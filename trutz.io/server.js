const fastify = require("fastify")({ logger: true });
const path = require("node:path");
const marked = require("marked");
const fs = require("fs");

fastify.register(require("@fastify/static"), {
  root: path.join(__dirname, "public"),
  prefix: "/public/",
});

fastify.register(require("@fastify/view"), {
  engine: {
    ejs: require("ejs"),
  },
});

fastify.get("/favicon.ico", async (req, reply) => {
  return reply.sendFile("favicon.ico");
});

fastify.get("/*", async (req, reply) => {
  const urlPath = req.url === "/" ? "/README" : req.url;
  const mdFilePath = path.join(__dirname, "blog", urlPath + ".md");
  if (!fs.existsSync(mdFilePath)) {
    return reply.status(404).send("Markdown file not found");
  }
  try {
    const mdContent = await fs.promises.readFile(mdFilePath, "utf-8");
    const htmlContent = marked.parse(mdContent, { gfm: true });
    return reply.view("index.ejs", { content: htmlContent });
  } catch (err) {
    fastify.log.error(err);
    return reply.status(500).send("Internal Server Error");
  }
});

fastify.listen({ port: 3000, host: "0.0.0.0" }, function (err, address) {
  if (err) {
    fastify.log.error(err);
    process.exit(1);
  }
});
