package com.example.listview

import android.app.Activity
import android.app.DownloadManager
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.SimpleAdapter
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_READ_CONTACTS=1
    private var READ_CONTACTS_GRANTED =false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //получаем разрешение
        val hasReaContactPermission=
            ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_CONTACTS)
        //если устройство до API 23 то устонавливаем разрешение
        if(hasReaContactPermission == PackageManager.PERMISSION_GRANTED){
            READ_CONTACTS_GRANTED=true
        }else{// вызов диологового окна для разрешения
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_CONTACTS),
                REQUEST_CODE_READ_CONTACTS)
        }
        //если разрешение устоновленно загружаем контакты
        if(READ_CONTACTS_GRANTED){
            loadContacts()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_READ_CONTACTS->{
                //разрешение получено
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadContacts()
                }else{// разрешение не получего
                    Toast.makeText(this,"Вам необходимо дать разрешение",Toast.LENGTH_LONG).show()
                    startActivity(Intent(this,MainActivity::class.java))
                }
                return
            }
        }
    }
    private  fun loadContacts(){
        val cursor:Cursor? =contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,null,null,null)
        startManagingCursor(cursor)

        val  from = arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone._ID)
        val to = intArrayOf(android.R.id.text1,android.R.id.text2)
        val simple =SimpleCursorAdapter(this,android.R.layout.simple_list_item_2,cursor,from,to)
        lv_contacts.adapter =simple
    }
}
