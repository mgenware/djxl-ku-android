package com.mgenware.djxlku

/**
 * DjxlKu is a Kotlin wrapper for the native djxl CLI.
 */
class DjxlKu {
    private external fun djxlmain(argv: Array<String>): Int

    companion object {
        init {
            System.loadLibrary("djxlx")

            // Load this lib last.
            System.loadLibrary("djxlku")
        }
    }

    /**
     * Runs the djxl CLI with the provided arguments.
     *
     * @param argv The command line arguments to pass to djxl.
     * @return The exit code from the djxl CLI.
     */
    fun run(argv: Array<String>): Int {
        val newArgv = arrayOf("djxl") + argv
        return djxlmain(newArgv)
    }
}