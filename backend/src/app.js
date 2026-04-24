const express = require("express");
const cors = require("cors");
const helmet = require("helmet");
const morgan = require("morgan");
const compression = require("compression");
const rateLimit = require("express-rate-limit");
const ApiError = require("./utils/apiError");
const passport = require("./config/passport");
const session = require("express-session");

const app = express();

// Session middleware for OAuth
app.use(session({
    secret: process.env.SESSION_SECRET || "focusappsecret",
    resave: false,
    saveUninitialized: false,
}));
app.use(passport.initialize());
app.use(passport.session());

const parsedCorsOrigins = (process.env.CORS_ORIGIN || "*")
    .split(",")
    .map((value) => value.trim())
    .filter(Boolean);

// Security Middlewares
app.use(helmet());
app.use(cors({
    origin: (origin, callback) => {
        if (!origin) {
            return callback(null, true);
        }

        if (parsedCorsOrigins.includes("*") || parsedCorsOrigins.includes(origin)) {
            return callback(null, true);
        }

        return callback(new ApiError(403, "CORS policy does not allow this origin"));
    },
    credentials: true
}));

// Performance Middlewares
app.use(compression());
app.use(express.json({ limit: "16kb" }));
app.use(express.urlencoded({ extended: true, limit: "16kb" }));

// Logging
if (process.env.NODE_ENV === "development") {
    app.use(morgan("dev"));
}

// Rate Limiting
const limiter = rateLimit({
    windowMs: 15 * 60 * 1000, // 15 minutes
    max: 100, // limit each IP to 100 requests per windowMs
    message: "Too many requests from this IP, please try again after 15 minutes"
});
app.use("/api", limiter);

// Routes import
const authRouter = require("./routes/auth.routes");
const focusLockRouter = require("./routes/focusLock.routes");
const blockedAppsRouter = require("./routes/blockedApps.routes");
const sessionsRouter = require("./routes/sessions.routes");
const remindersRouter = require("./routes/reminders.routes");

// Routes declaration
app.use("/api/v1/auth", authRouter);
app.use("/api/v1/focus-lock", focusLockRouter);

// Explicit aliases for mobile/client integrations.
app.use("/api/v1/blocked-apps", blockedAppsRouter);
app.use("/api/v1/sessions", sessionsRouter);
app.use("/api/v1/reminders", remindersRouter);

app.get("/health", (_, res) => {
    res.status(200).json({ success: true, message: "Backend is healthy" });
});

// 404 handler
app.use((req, res, next) => {
    next(new ApiError(404, "Resource not found"));
});

// Centralized Error Middleware
app.use((err, req, res, next) => {
    const statusCode = err.statusCode || 500;
    const message = err.message || "Internal Server Error";
    
    res.status(statusCode).json({
        success: false,
        message,
        errors: err.errors || [],
        stack: process.env.NODE_ENV === "development" ? err.stack : undefined
    });
});

module.exports = app;
