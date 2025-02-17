# RateLimiting

This repository is intended for study purposes only. Please avoid copying and pasting it directly into your projects.

Also check https://levelup.gitconnected.com/top-5-rate-limiting-tactics-for-optimal-traffic-5ea77fd4461c

- Token Bucket
  - The bucket contains tokens that are added at a constant rate.
  - Each request consumes a certain number of tokens.
  - If the bucket has enough tokens, the request is allowed; otherwise, it is rejected.
- Leaky Bucket
  - The bucket contains requests that are leaked (sent) at a constant rate.
  - Each request is like a drop, and the bucket leaks at a rate of N drops per second.
  - The bucket has a maximum capacity of M drops. New requests are rejected if the bucket overflows.
  - The leaking property ensures that requests are sent/processed evenly over time.
- Fixed Window Counter
  - Time is divided into fixed windows. Let's say 10 seconds.
  - During every time window, only N requests can be processed. All excess requests are rejected.
  - Immediately after a new window starts, it can process N requests again (burst-friendly).
- Sliding Log
  - The window slides continuously.
  - It ensures the window has a maximum of N requests at any time.
  - Every new incoming request pushes the window to the right.
  - The window slides, and some old requests are now outside the window. 