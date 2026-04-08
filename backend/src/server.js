require("dotenv").config();
const connectDB = require("./config/db");
const app = require("./app");

const requiredEnvVars = ["MONGODB_URI", "JWT_SECRET", "JWT_EXPIRE"];
const missingEnvVars = requiredEnvVars.filter((name) => !process.env[name]);

if (missingEnvVars.length) {
  console.error(`Missing required environment variables: ${missingEnvVars.join(", ")}`);
  process.exit(1);
}

const PORT = process.env.PORT || 5000;

connectDB()
  .then(() => {
    app.listen(PORT, () => {
      console.log(`\nServer is running at port : ${PORT}`);
      console.log(`Environment: ${process.env.NODE_ENV}`);
    });
  })
  .catch((err) => {
    console.log("MONGODB connection failed !!! ", err);
  });
