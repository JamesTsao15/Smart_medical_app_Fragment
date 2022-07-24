package com.example.smart_medical_app.ui.remind

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smart_medical_app.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList

class RemindFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root=inflater.inflate(R.layout.fragment_remind,null)

        return root
    }
    override fun onResume() {
        super.onResume()

    }


}