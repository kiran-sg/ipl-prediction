const { chromium } = require('playwright');

const YEAR = 2026;
const MONTHS = { JAN: 0, FEB: 1, MAR: 2, APR: 3, MAY: 4, JUN: 5, JUL: 6, AUG: 7, SEP: 8, OCT: 9, NOV: 10, DEC: 11 };

function parseDateTime(raw) {
  // "MAR, SAT 28 7:30 pm IST" -> "2026-03-28T19:30:00+05:30"
  const match = raw.match(/([A-Z]{3}),?\s*\w+\s+(\d+)\s+(\d+):(\d+)\s*(am|pm)\s*IST/i);
  if (!match) return raw;
  const [, mon, day, hourStr, min, ampm] = match;
  let hour = parseInt(hourStr);
  if (ampm.toLowerCase() === 'pm' && hour !== 12) hour += 12;
  if (ampm.toLowerCase() === 'am' && hour === 12) hour = 0;
  const month = MONTHS[mon.toUpperCase()];
  if (month === undefined) return raw;
  const d = String(parseInt(day)).padStart(2, '0');
  const m = String(month + 1).padStart(2, '0');
  const h = String(hour).padStart(2, '0');
  const mi = String(parseInt(min)).padStart(2, '0');
  return `${YEAR}-${m}-${d}T${h}:${mi}:00+05:30`;
}

(async () => {
  const browser = await chromium.launch({ headless: false });
  const context = await browser.newContext({ viewport: { width: 1920, height: 1080 } });
  const page = await context.newPage();

  console.log('Opening IPL fixtures page...');
  await page.goto('https://www.iplt20.com/matches/fixtures', { waitUntil: 'networkidle', timeout: 60000 });
  await page.waitForSelector('.vn-shedTeam', { timeout: 15000 }).catch(() => {});

  for (let i = 0; i < 30; i++) {
    await page.evaluate(() => window.scrollBy(0, 800));
    await page.waitForTimeout(400);
  }
  await page.waitForTimeout(2000);

  const matches = await page.evaluate(() => {
    const cards = document.querySelectorAll('.vn-shedule-desk');
    return Array.from(cards).map(card => {
      const teams = card.querySelectorAll('.vn-teamName');
      const date = card.querySelector('.vn-matchDate')?.textContent?.trim() || '';
      const time = card.querySelector('.vn-matchTime')?.textContent?.trim() || '';
      const head = card.parentElement?.querySelector('.vn-schedule-head')?.textContent?.trim() || '';
      const matchNoMatch = head.match(/Match\s+(\d+)/i);
      return {
        matchNo: matchNoMatch ? matchNoMatch[1] : '',
        home: teams[0]?.textContent?.trim() || '',
        away: teams[1]?.textContent?.trim() || '',
        dateTime: (date + ' ' + time).trim()
      };
    }).filter(m => m.home && m.away);
  });

  // Convert date format
  matches.forEach(m => { m.dateTime = parseDateTime(m.dateTime); });

  console.log(`Found ${matches.length} matches`);
  if (matches.length > 0) {
    console.log('Sample:', JSON.stringify(matches.slice(0, 3), null, 2));
  }

  if (matches.length > 0) {
    const API_URL = process.env.API_URL || 'http://localhost:8080';
    try {
      const response = await fetch(`${API_URL}/api/matches/sync`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(matches)
      });
      const result = await response.json();
      console.log('Sync result:', result);
    } catch (err) {
      console.error('Failed to sync to API:', err.message);
      console.log('\nFull match data:');
      console.log(JSON.stringify(matches, null, 2));
    }
  }

  await browser.close();
})();
