const mongoose = require("mongoose");
const { FOCUS_THEMES } = require("../constants");

const preferenceSchema = new mongoose.Schema(
  {
    userId: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "User",
      required: true,
      unique: true,
    },
    darkMode: {
      type: Boolean,
      default: true,
    },
    accentTheme: {
      type: String,
      enum: Object.values(FOCUS_THEMES),
      default: FOCUS_THEMES.DEEP_WORK,
    },
    defaultDuration: {
      type: Number,
      default: 25,
    },
    strictModeDefault: {
      type: Boolean,
      default: false,
    },
    hapticsEnabled: {
      type: Boolean,
      default: true,
    },
    soundEnabled: {
      type: Boolean,
      default: true,
    },
  },
  {
    timestamps: true,
  }
);

module.exports = mongoose.model("Preference", preferenceSchema);
