package com.mgenware.djxlkuapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.documentfile.provider.DocumentFile
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import com.mgenware.djxlku.DjxlKu
import com.mgenware.djxlkuapp.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                // Filter jxl files
                type = "image/jxl"
            }
            startActivityForResult(intent, 111)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                val documentFile = DocumentFile.fromSingleUri(this, uri) ?: throw IOException("Cannot open document file")
                val inputFile = copyToTempFile(documentFile)
                val tmpFile = File.createTempFile("tmp", ".jpg")
                val outFile = tmpFile.absolutePath
                val djxlKu = DjxlKu();
                val args = arrayOf<String>(
                    inputFile,
                    outFile
                )
                val result = djxlKu.run(args)
                val outFileExist = tmpFile.exists()
                if (result != 0) {
                    Snackbar.make(binding.root, "Error", Snackbar.LENGTH_LONG)
                        .show()
                } else if (!outFileExist) {
                    Snackbar.make(binding.root, "Output file does not exist", Snackbar.LENGTH_LONG)
                        .show()
                } else {
                    Snackbar.make(binding.root, "Success", Snackbar.LENGTH_LONG)
                        .show()
                    val outUri = FileProvider.getUriForFile(
                        this@MainActivity,
                        "com.mgenware.djxlkuapp.provider",
                        tmpFile
                    )

                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(outUri, "image/jpeg")
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent)
                }
            }
        }
    }

    private fun copyToTempFile(df: DocumentFile): String {
        val inputStream = contentResolver.openInputStream(df.uri) ?: throw IOException("Cannot open input stream")
        val fileName = "tempfile_" + System.currentTimeMillis() + df.name
        val tempFile = File(cacheDir, fileName)

        inputStream.use { input ->
            FileOutputStream(tempFile).use { output ->
                input.copyTo(output)
            }
        }
        return tempFile.absolutePath
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}