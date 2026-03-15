FROM dhi.io/node:25-dev
EXPOSE 3000

WORKDIR /app
COPY package.json ./
ENV NODE_ENV=production
RUN npm install
COPY public ./public
COPY server.js ./
COPY index.ejs ./
CMD ["node", "server.js"]

COPY --exclude=.obsidian/ blog ./blog