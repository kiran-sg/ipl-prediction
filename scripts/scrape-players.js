const { chromium } = require('playwright');

const TEAM_CODE_MAP = {
  'chennai-super-kings': 'CSK',
  'delhi-capitals': 'DC',
  'gujarat-titans': 'GT',
  'kolkata-knight-riders': 'KKR',
  'lucknow-super-giants': 'LSG',
  'mumbai-indians': 'MI',
  'punjab-kings': 'PBKS',
  'rajasthan-royals': 'RR',
  'royal-challengers-bengaluru': 'RCB',
  'sunrisers-hyderabad': 'SRH'
};

(async () => {
  const browser = await chromium.launch({ headless: false });
  const context = await browser.newContext({ viewport: { width: 1920, height: 1080 } });
  const page = await context.newPage();

  console.log('Opening IPL teams page...');
  await page.goto('https://www.iplt20.com/teams', { waitUntil: 'networkidle', timeout: 60000 });
  await page.waitForTimeout(3000);

  // Get team URLs
  const teamUrls = await page.evaluate(() => {
    const links = document.querySelectorAll('a[href*="/teams/"]');
    const seen = new Set();
    return Array.from(links)
      .map(a => a.href)
      .filter(href => {
        if (seen.has(href) || !href.match(/\/teams\/[a-z-]+$/)) return false;
        seen.add(href);
        return true;
      });
  });

  console.log(`Found ${teamUrls.length} teams`);
  const allPlayers = [];
  let playerNo = 1;

  for (const url of teamUrls) {
    const slug = url.split('/teams/')[1];
    const teamCode = TEAM_CODE_MAP[slug] || slug.toUpperCase();
    console.log(`Scraping ${teamCode}...`);

    await page.goto(url, { waitUntil: 'networkidle', timeout: 30000 });
    await page.waitForSelector('.ih-p-name', { timeout: 10000 }).catch(() => {});
    await page.evaluate(() => window.scrollTo(0, document.body.scrollHeight));
    await page.waitForTimeout(3000);

    const players = await page.evaluate(() => {
      return Array.from(document.querySelectorAll('.ih-p-img')).map(card => {
        const name = card.querySelector('.ih-p-name')?.textContent?.trim() || '';
        const fullText = card.textContent.trim();
        const role = fullText.replace(name, '').trim();
        const img = card.querySelector('img');
        const imageUrl = img ? (img.src || img.getAttribute('data-src') || '') : '';
        return { name, role, imageUrl };
      }).filter(p => p.name);
    });

    for (const p of players) {
      allPlayers.push({
        playerNo: String(playerNo++),
        playerName: p.name,
        category: p.role,
        team: teamCode,
        imageUrl: p.imageUrl
      });
    }
    console.log(`  ${players.length} players`);
  }

  console.log(`\nTotal: ${allPlayers.length} players`);

  if (allPlayers.length > 0) {
    const API_URL = process.env.API_URL || 'http://localhost:8080';
    try {
      const response = await fetch(`${API_URL}/api/players/sync`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(allPlayers)
      });
      const result = await response.json();
      console.log('Sync result:', result);
    } catch (err) {
      console.error('Failed to sync to API:', err.message);
      console.log('\nFull player data:');
      console.log(JSON.stringify(allPlayers, null, 2));
    }
  }

  await browser.close();
})();
