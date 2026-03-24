# IPL Prediction App Backend

A Spring Boot backend for an IPL match prediction game. Users predict match outcomes and earn points based on results.

## Tech Stack

- Java 17+ / Spring Boot
- PostgreSQL 16
- Playwright (local scrapers)
- CricAPI (automated match results)

## Database Setup

PostgreSQL runs in Docker via WSL2:

```bash
docker compose up -d
```

This starts a PostgreSQL 16 instance on port `5432` with default credentials (`postgres/postgres`). Tables are auto-created by Hibernate on startup.

### Tables

| Table | Description |
|-------|-------------|
| `ipl_users` | Users with name, location, admin flag |
| `ipl_teams` | Team names, short codes, logo URLs |
| `ipl_players` | Player details, category, team, image URL |
| `ipl_matches` | Match schedule with date/time, home/away teams |
| `predictions` | User predictions per match |
| `tournament_predictions` | User tournament-level predictions |

## Running the App

```bash
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Duser.timezone=Asia/Kolkata"
```

> The timezone flag is required to avoid `Asia/Calcutta` errors with PostgreSQL 16.

For IntelliJ, add `-Duser.timezone=Asia/Kolkata` to your run configuration VM options.

## API Endpoints

### Public

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/matches` | Get all matches |
| GET | `/api/teams` | Get all teams |
| POST | `/api/players` | Get players (filter by teams) |
| POST | `/api/users/validate` | Login / validate user |

### Admin (requires `isAdmin = true`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/admin/predictions/match?matchId=` | Get predictions for a match |
| GET | `/api/admin/match/result?matchId=` | Get match result |
| POST | `/api/admin/match/result` | Update match result |
| POST | `/api/admin/sync/results` | Trigger CricAPI results sync |
| POST | `/api/admin/prediction/delete` | Delete predictions |

### Sync (used by scrapers)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/teams/sync` | Upsert teams |
| POST | `/api/players/sync` | Upsert players |
| POST | `/api/matches/sync` | Upsert match schedule |

## Scrapers

Located in `scripts/`. These run locally on Windows using Playwright (iplt20.com blocks headless/server requests via Akamai).

### Setup

```bash
cd scripts
npm install
npx playwright install chromium
```

### Running

```bash
# Local (default: http://localhost:8080)
node scrape-teams.js
node scrape-players.js
node scrape-fixtures.js

# Against production
API_URL=https://your-app.up.railway.app node scrape-teams.js
API_URL=https://your-app.up.railway.app node scrape-players.js
API_URL=https://your-app.up.railway.app node scrape-fixtures.js
```

| Script | Source | Data |
|--------|--------|------|
| `scrape-teams.js` | iplt20.com/teams | Team names, short codes, logos |
| `scrape-players.js` | iplt20.com/teams/{team} | Player names, roles, images |
| `scrape-fixtures.js` | iplt20.com schedule | Match numbers, dates, venues, teams |

All scrapers open a real browser window (non-headless) to bypass bot detection, scrape the data, and POST it to the sync API endpoints.

## Automated Match Results

Match results are fetched from [CricAPI](https://cricapi.com) (works on Railway, no browser needed).

- **Scheduled**: Runs daily at 11:30 PM IST
- **On-demand**: `POST /api/admin/sync/results` (admin only)

### Configuration

Set your CricAPI key:

- Local: `cricapi.key=YOUR_KEY` in `application-dev.properties`
- Railway: Set `CRICAPI_KEY` environment variable

Get a free API key at https://api.cricapi.com/

## Deployment

The app deploys as two services on Railway:

1. **Backend** — this Spring Boot app
2. **UI** — frontend app (separate repo)

Environment variables needed on Railway:

| Variable | Description |
|----------|-------------|
| `SPRING_DATASOURCE_URL` | PostgreSQL connection URL |
| `SPRING_DATASOURCE_USERNAME` | DB username |
| `SPRING_DATASOURCE_PASSWORD` | DB password |
| `CRICAPI_KEY` | CricAPI key for match results |

## Testing

```bash
./mvnw test -Duser.timezone=Asia/Kolkata
```

Tests use an in-memory H2 database (configured in `application-test.properties`).
