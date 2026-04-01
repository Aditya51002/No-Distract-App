const mongoose = require("mongoose");
const { FOCUS_THEMES } = require("../constants");

const sessionConfigSchema = new mongoose.Schema(
  {
    userId: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "User",
      required: true,
      unique: true,
    },
    duration: {
      type: Number,
      default: 25,
    },
    strictMode: {
      type: Boolean,
      default: false,
    },
    notificationsMuted: {
      type: Boolean,
      default: true,
    },
    breakReminder: {
      type: Boolean,
      default: true,
    },
    theme: {
      type: String,
      enum: Object.values(FOCUS_THEMES),
      default: FOCUS_THEMES.DEEP_WORK,
    },
    selectedApps: [
      {
        type: mongoose.Schema.Types.ObjectId,
        ref: "BlockedApp",
      },
    ],
  },
  {
    timestamps: true,
  }
);

module.exports = mongoose.model("SessionConfig", sessionConfigSchema);
