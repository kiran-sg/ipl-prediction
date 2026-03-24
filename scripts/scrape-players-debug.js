const { chromium } = require('playwright');

(async () => {
  const browser = await chromium.launch({ headless: false });
  const context = await browser.newContext({ viewport: { width: 1920, height: 1080 } });
  const page = await context.newPage();

  console.log('Opening IPL teams page...');
  await page.goto('https://www.iplt20.com/teams', { waitUntil: 'networkidle', timeout: 60000 });
  await page.waitForTimeout(3000);

  // Get team links
  const teamLinks = await page.evaluate(() => {
    const links = document.querySelectorAll('a[href*="/teams/"]');
    const seen = new Set();
    return Array.from(links).map(a => ({
      href: a.href,
      text: a.textContent.trim().substring(0, 100),
      class: a.className
    })).filter(l => {
      if (seen.has(l.href) || !l.href.includes('/teams/')) return false;
      seen.add(l.href);
      return true;
    });
  });
  console.log('Team links:', JSON.stringify(teamLinks, null, 2));

  // Click first team to inspect player page structure
  if (teamLinks.length > 0) {
    const firstTeamUrl = teamLinks.find(t => t.href.match(/\/teams\/[a-z-]+$/))?.href;
    if (firstTeamUrl) {
      console.log('\nNavigating to:', firstTeamUrl);
      await page.goto(firstTeamUrl, { waitUntil: 'networkidle', timeout: 30000 });
      await page.waitForTimeout(3000);

      const classes = await page.evaluate(() => {
        const all = document.querySelectorAll('*');
        const cls = new Set();
        all.forEach(el => el.classList.forEach(c => {
          const l = c.toLowerCase();
          if (l.includes('player') || l.includes('squad') || l.includes('roster') || l.includes('team'))
            cls.add(c);
        }));
        return Array.from(cls).sort();
      });
      console.log('Player-related classes:', JSON.stringify(classes, null, 2));

      // Dump samples of player elements
      const debug = await page.evaluate(() => {
        const info = {};
        ['.ih-p-name', '.player-name', '.ih-p-img', '.ih-pcard', '.ih-pt-cont',
         '[class*=player]', '[class*=squad]'].forEach(sel => {
          const els = document.querySelectorAll(sel);
          if (els.length > 0) {
            info[sel] = {
              count: els.length,
              samples: Array.from(els).slice(0, 2).map(e => ({
                tag: e.tagName,
                text: e.textContent.trim().substring(0, 150),
                class: e.className,
                parentClass: e.parentElement?.className || ''
              }))
            };
          }
        });
        return info;
      });
      console.log('Player elements:', JSON.stringify(debug, null, 2));
    }
  }

  await browser.close();
})();
