# Read Me First

Created three branch structure to be able to implement solution fast.


calendar - The Calendar API to get user meetings and attendees

person - Person Data API to call external paid API and cache it (temporary H2, but ideally Redis/NOSQL)

email - CronJob to fetch all data, create email content and send it (didn't create the HTML, front end is not my strongest skill)

One feature will be missing: meeting counter for other UserGems members with the customer.
To make that work, would change the email cron to first get all the required data (and probably cache it too), then count all meetings and send all at once instead of one at time like it works now.