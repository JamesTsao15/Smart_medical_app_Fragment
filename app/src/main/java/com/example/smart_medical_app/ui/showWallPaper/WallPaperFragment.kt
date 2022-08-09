package com.example.smart_medical_app.ui.showWallPaper

import android.app.WallpaperManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.smart_medical_app.R
import com.example.smart_medical_app.UserEmergencyInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class WallPaperFragment : Fragment() {
    private lateinit var img_showTheWallPaper:ImageView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var btn_setWallPaper:Button
    private lateinit var myWallpaperManager:WallpaperManager
    private lateinit var mAuth: FirebaseAuth
    private lateinit var bitmap:Bitmap
    private lateinit var displayMetrics: DisplayMetrics
    private var width:Int=0
    private var height:Int=0
    private var uid:String=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.fragment_wall_paper, null)
        mAuth= FirebaseAuth.getInstance()
        uid=mAuth.currentUser?.uid ?:""
        img_showTheWallPaper=root.findViewById(R.id.imageView_show_Information)
        myWallpaperManager= WallpaperManager.getInstance(requireContext())

        btn_setWallPaper=root.findViewById(R.id.button_setWallPaper)
        databaseReference= FirebaseDatabase.getInstance().getReference("Users_EmergencyInformation")
        databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val emergencyInfo=snapshot.getValue<UserEmergencyInfo>()
                img_showTheWallPaper.setImageBitmap(emergencyInfo?.let { textToBitmap(it) })
                bitmap= emergencyInfo?.let { textToBitmap(it) }!!

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),"載入失敗，$error", Toast.LENGTH_SHORT).show()
            }

        })
        myWallpaperManager= WallpaperManager.getInstance(requireContext())

        return root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onResume() {
        super.onResume()
        btn_setWallPaper.setOnClickListener {
            getScreenWidthHeight()
            setBitmapSize()
            myWallpaperManager= WallpaperManager.getInstance(requireContext())
            try {
                myWallpaperManager.setBitmap(bitmap,null,false,WallpaperManager.FLAG_LOCK)
                myWallpaperManager.suggestDesiredDimensions(width,height)
            }catch (e:IOException){
                e.printStackTrace()
            }
//            val path=getExtermalStoragePublicDir("medical_app_Wallpaper").path
//            Log.e("JAMES",path)
//            saveJPGE_After(bitmap,path,System.currentTimeMillis().toString()+".jpg")
        }
    }

    private fun setBitmapSize() {
        bitmap= Bitmap.createScaledBitmap(bitmap,width,height,false)
    }

    private fun getScreenWidthHeight() {
        displayMetrics= DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        width=displayMetrics.widthPixels
        height=displayMetrics.heightPixels
    }

    private fun textToBitmap(emergencyInfo: UserEmergencyInfo): Bitmap {
        val paint= Paint(Paint.ANTI_ALIAS_FLAG)
        val bitmap_x:Int=10
        var bitmap_y:Int=20
        paint.textSize=15F
        paint.setColor(Color.BLACK)
        paint.textAlign= Paint.Align.LEFT
        val text="姓名:"+emergencyInfo.personName+"\n"+
                "健康狀況:"+emergencyInfo.healthStatus+"\n"+
                "緊急聯絡人1:"+emergencyInfo.contactPerson1+"\n"+
                "緊急電話1:"+emergencyInfo.contactPhone1+"\n"+
                "緊急聯絡人2:"+emergencyInfo.contactPerson2+"\n"+
                "緊急電話2:"+emergencyInfo.contactPhone2+"\n"+
                "住址:"+emergencyInfo.address
        Log.e("JAMES",text)
        var image: Bitmap = Bitmap.createBitmap(320,200, Bitmap.Config.ARGB_8888)
        val canvas: Canvas = Canvas(image)
        canvas.drawColor(Color.WHITE)
        canvas.drawText("", bitmap_x.toFloat(), bitmap_y.toFloat(), paint)
        bitmap_y += (paint.descent() - paint.ascent()).toInt()
        for (line in text.split("\n").toTypedArray()) {
            canvas.drawText(line, bitmap_x.toFloat(), bitmap_y.toFloat(), paint)
            bitmap_y += (paint.descent() - paint.ascent()).toInt()
        }
       val m:Matrix= Matrix()
        m.postRotate(90f)
        image=Bitmap.createBitmap(image,0,0,image.width,image.height,m,true)

        return image
    }

    private fun getExtermalStoragePublicDir(FolderName:String):File{
        val file= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        if(!file.exists()){
            file.mkdir()
        }
        else{
            Log.e("JAMES","inLoopFile")
            val f=File(file,FolderName)
            if(!f.exists()){
                Log.e("JAMES","inLoop_f")
                f.mkdir()
                return f
            }
            else{
                return  File(file,FolderName)
            }
        }
        return  File(file,FolderName)
    }
    fun saveJPGE_After(bitmap: Bitmap, path: String,fileName:String) {
        val file = File(path,fileName)
        try {
            val out = FileOutputStream(file)
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush()
                out.close()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}