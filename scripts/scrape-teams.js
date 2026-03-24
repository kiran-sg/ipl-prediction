const { chromium } = require('playwright');

const TEAM_NAME_MAP = {
  'chennai-super-kings': { teamName: 'Chennai Super Kings', shortName: 'CSK' },
  'delhi-capitals': { teamName: 'Delhi Capitals', shortName: 'DC' },
  'gujarat-titans': { teamName: 'Gujarat Titans', shortName: 'GT' },
  'kolkata-knight-riders': { teamName: 'Kolkata Knight Riders', shortName: 'KKR' },
  'lucknow-super-giants': { teamName: 'Lucknow Super Giants', shortName: 'LSG' },
  'mumbai-indians': { teamName: 'Mumbai Indians', shortName: 'MI' },
  'punjab-kings': { teamName: 'Punjab Kings', shortName: 'PBKS' },
  'rajasthan-royals': { teamName: 'Rajasthan Royals', shortName: 'RR' },
  'royal-challengers-bengaluru': { teamName: 'Royal Challengers Bengaluru', shortName: 'RCB' },
  'sunrisers-hyderabad': { teamName: 'Sunrisers Hyderabad', shortName: 'SRH' }
};

(async () => {
  const browser = await chromium.launch({ headless: false });
  const context = await browser.newContext({ viewport: { width: 1920, height: 1080 } });
  const page = await context.newPage();

  console.log('Opening IPL teams page...');
  await page.goto('https://www.iplt20.com/teams', { waitUntil: 'networkidle', timeout: 60000 });
  await page.evaluate(() => window.scrollTo(0, document.body.scrollHeight));
  await page.waitForTimeout(3000);

  const teams = await page.evaluate(() => {
    const links = document.querySelectorAll('a[href*="/teams/"]');
    const seen = new Set();
    const results = [];
    for (const a of links) {
      const href = a.href;
      if (seen.has(href) || !href.match(/\/teams\/[a-z-]+$/)) continue;
      seen.add(href);
      const slug = href.split('/teams/')[1];
      const img = a.querySelector('img');
      const logoUrl = img ? (img.src || img.getAttribute('data-src') || '') : '';
      results.push({ slug, logoUrl });
    }
    return results;
  });

  const allTeams = teams.map(t => {
    const mapped = TEAM_NAME_MAP[t.slug];
    if (!mapped) return null;
    return { ...mapped, logoUrl: t.logoUrl };
  }).filter(Boolean);

  console.log(`Found ${allTeams.length} teams`);
  allTeams.forEach(t => console.log(`  ${t.shortName}: ${t.teamName} - ${t.logoUrl ? 'has logo' : 'NO LOGO'}`));

  if (allTeams.length > 0) {
    const API_URL = process.env.API_URL || 'http://localhost:8080';
    try {
      const response = await fetch(`${API_URL}/api/teams/sync`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(allTeams)
      });
      const result = await response.json();
      console.log('Sync result:', result);
    } catch (err) {
      console.error('Failed to sync to API:', err.message);
      console.log(JSON.stringify(allTeams, null, 2));
    }
  }

  await browser.close();
})();
