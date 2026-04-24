require("dotenv").config();
const connectDB = require("./config/db");
const app = require("./app");
const http = require("http");
const { Server } = require("socket.io");

const requiredEnvVars = ["MONGODB_URI", "JWT_SECRET", "JWT_EXPIRE"];
const missingEnvVars = requiredEnvVars.filter((name) => !process.env[name]);

if (missingEnvVars.length) {
  console.error(`Missing required environment variables: ${missingEnvVars.join(", ")}`);
  process.exit(1);
}

const PORT = process.env.PORT || 5000;

const server = http.createServer(app);
const io = new Server(server, {
  cors: {
    origin: "*",
    methods: ["GET", "POST", "PATCH", "DELETE"],
  },
});

// Make io available to routes/controllers
app.set("io", io);

connectDB()
  .then(() => {
    server.listen(PORT, () => {
      console.log(`\nServer is running at port : ${PORT}`);
      console.log(`Environment: ${process.env.NODE_ENV}`);
    });
  })
  .catch((err) => {
    console.log("MONGODB connection failed !!! ", err);
  });

// Optionally, handle connection events
io.on("connection", (socket) => {
  console.log("A user connected: " + socket.id);
  socket.on("disconnect", () => {
    console.log("User disconnected: " + socket.id);
  });
});
