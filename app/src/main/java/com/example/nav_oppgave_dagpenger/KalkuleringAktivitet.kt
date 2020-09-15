package com.example.nav_oppgave_dagpenger

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.set
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.aktivitet_kalkulering.*
import java.lang.Exception
import java.util.*
import kotlin.math.ceil
import kotlin.math.max
import android.util.Log

class KalkuleringAktivitet : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.aktivitet_kalkulering)

        //Instans av kalkuleringsklassen vår
        val kalk = KalkuleringKlasse()

        //Kjører testfunksjon som tester kalkuleringsmetoden med diverse verdier.
        Log.d("Testkalkulering_TAG", "kjoerer test")
        Log.d("Testkalkulering_TAG", kalk.kjoerTest())

        //Design-elementer
        val aarfelt1 = findViewById<TextView>(R.id.aarfelt1)
        val aarfelt2 = findViewById<TextView>(R.id.aarfelt2)
        val aarfelt3 = findViewById<TextView>(R.id.aarfelt3)
        val lonnFelt1 = findViewById<EditText>(R.id.lonnfelt1)
        val lonnFelt2 = findViewById<EditText>(R.id.lonnfelt2)
        val lonnFelt3 = findViewById<EditText>(R.id.lonnfelt3)
        val kalkulerKnapp = findViewById<Button>(R.id.kalkulerKnapp)
        val omstartKnapp = findViewById<Button>(R.id.omstartKnapp)
        val hjelpKnapp = findViewById<FloatingActionButton>(R.id.hjelpKnapp)
        val resultatFelt = findViewById<EditText>(R.id.resultatFelt)

        //Regner ut hvilke år som skal stå i input-feltene basert på dagens dato.
        val dagensAar= Calendar.getInstance().get(Calendar.YEAR);
        aarfelt1.text = ((dagensAar-1).toString())
        aarfelt2.text = ((dagensAar-2).toString())
        aarfelt3.text = ((dagensAar-3).toString())

        //Spørsmålstegnknapp skal vise et felt som forklarer dagpengeordningen
        hjelpKnapp.setOnClickListener {
            try {
                val alertDialogView =
                    LayoutInflater.from(this).inflate(R.layout.hjelp_alertdialog, null)
                val builder = AlertDialog.Builder(this)
                    .setView(alertDialogView)
                    .setTitle("Hva er dagpenger?")
                val tmpalertdialog = builder.show()
            }catch (ex: Exception){

            }
        }

        //Kalkuleringsknappen skal regne ut potensielle dagpenger og fremstille resultatet i resultat-feltet under
        kalkulerKnapp.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.setBackgroundColor(getColor(R.color.colorLightAccent))
                }

                MotionEvent.ACTION_UP -> {
                    if (lonnFelt1.text.toString() == ""){
                        lonnFelt1.setText("0")
                    }
                    if (lonnFelt2.text.toString() == ""){
                        lonnFelt2.setText("0")
                    }
                    if (lonnFelt3.text.toString() == ""){
                        lonnFelt3.setText("0")
                    }
                    val verdi1 = lonnFelt1.text.toString().toLong()
                    val verdi2 = lonnFelt2.text.toString().toLong()
                    val verdi3 = lonnFelt3.text.toString().toLong()
                    val resultat = kalk.kalkulerDagpenger(verdi1, verdi2, verdi3)
                    if(resultat == "0"){
                        resultatFelt.setText(R.string.ikkeKrav)
                    }
                    else {
                        resultatFelt.setText("Du har krav på:\n"+ resultat+"kr/dag")
                    }
                    v.setBackgroundColor(getColor(R.color.colorAccent))
                }
            }
            false

        }

        //Omstartknapp skal tømme alle inputs- og resultatstrenger
        omstartKnapp.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.setBackgroundColor(getColor(R.color.colorLightGray))
                }

                MotionEvent.ACTION_UP -> {
                    lonnFelt1.text = null
                    lonnFelt2.text = null
                    lonnFelt3.text = null
                    resultatFelt.text = null
                    v.setBackgroundColor(getColor(R.color.colorGray))
                }
            }
            false
        }
    }
}