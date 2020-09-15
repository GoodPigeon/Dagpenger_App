package com.example.nav_oppgave_dagpenger

import android.graphics.ColorFilter
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import java.util.*
import kotlin.math.ceil
import kotlin.math.max

class KalkuleringAktivitet : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.aktivitet_kalkulering)

        //Konstante verdier
        val grunnbelop:Double = 101351.0 //Per 1. Mai 2020, kan automatiseres dersom man har tilgjengelig database
        val maxArbeidsdager:Double = 260.0

        //Design-elementer
        val aarfelt1 = findViewById<TextView>(R.id.aarfelt1)
        val aarfelt2 = findViewById<TextView>(R.id.aarfelt2)
        val aarfelt3 = findViewById<TextView>(R.id.aarfelt3)
        val lonnFelt1 = findViewById<EditText>(R.id.lonnfelt1)
        val lonnFelt2 = findViewById<EditText>(R.id.lonnfelt2)
        val lonnFelt3 = findViewById<EditText>(R.id.lonnfelt3)
        val kalkulerKnapp = findViewById<Button>(R.id.kalkulerKnapp)
        val omstartKnapp = findViewById<Button>(R.id.omstartKnapp)
        val spoersmaalKnapp = findViewById<FloatingActionButton>(R.id.spoersmaalKnapp)
        val resultatFelt = findViewById<EditText>(R.id.resultatFelt)

        //Regner ut hvilke år som skal stå i input-feltene basert på dagens dato.
        val dagensAar= Calendar.getInstance().get(Calendar.YEAR);
        aarfelt1.text = ((dagensAar-1).toString())
        aarfelt2.text = ((dagensAar-2).toString())
        aarfelt3.text = ((dagensAar-3).toString())

        /*
        Funksjon for å regne ut eventuelle dagpenger man har krav på
            - Finner sammenlagt lønn over 3 siste årene og sammenlikner med 3G
            - Finner lønn siste året og sammenlikner med 1.5G
            - Sjekker også om man har hatt arbeidsinntekt siste kalenderåret. (Representert ved  verdiAar1 > 0)
            - Dersom en av disse er større enn 6G, bruker vi verdien 6G for å kalkulere dagsatsen (oppnådd høyeste dagsats)
            - Dersom begge verdier er mindre enn 6G, bruker vi den høyeste av disse for å kalkulere dagsatsen
        */
        fun kalkulerDagpenger(verdiAar1: Long, verdiAar2: Long, verdiAar3: Long): String {

            //Finner om man har tjent nok
            val kvalifisert1 = (verdiAar1 + verdiAar2 + verdiAar3) > (3 * grunnbelop)
            val kvalifisert2 = verdiAar1 > (1.5 * grunnbelop)
            val kvalifisert3 = verdiAar1 > 0

            if(!kvalifisert3) {
                return (getString(R.string.ikkeKrav))
            }
            else if(!kvalifisert1 && !kvalifisert2){
                return (getString(R.string.ikkeKrav))
            }

            //Dersom man har kvalifisert seg, vil dagsats regnes ut
            else{
                val dagpengegrunnlag1 = verdiAar1
                val dagpengegrunnlag2 = (verdiAar1+verdiAar2+verdiAar3)/3

                //Et grunnlag er større enn 6G, gir max dagpenger
                if(dagpengegrunnlag1 >= (6*grunnbelop) || dagpengegrunnlag2 >= (6*grunnbelop)){
                    return("Du har krav på\n" + (ceil((6*grunnbelop)/maxArbeidsdager)).toLong().toString() + " kr/dag")
                }
                //Ellers regnes dagpenger ut fra størst grunnlag
                else{
                    val storstSats  = max(dagpengegrunnlag1, dagpengegrunnlag2)
                    return("Du har krav på\n" + (ceil(storstSats/maxArbeidsdager)).toLong().toString() + " kr/dag")
                }
            }
        }

        //Spørsmålstegn skal vise et felt som forklarer dagpengeordningen

        //Kalkuleringsknappen skal regne ut potensielle dagpenger og fremstille resultatet i feltet under
        kalkulerKnapp.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.setBackgroundColor(getColor(R.color.colorLightAccent))
                }

                MotionEvent.ACTION_UP -> {
                    v.setBackgroundColor(getColor(R.color.colorAccent))
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
                    val resultat = kalkulerDagpenger(verdi1, verdi2, verdi3)
                    resultatFelt.setText(resultat)
                }
            }
            false

        }

        //Omstartknapp skal tømme alle inputs og resultat
        omstartKnapp.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.setBackgroundColor(getColor(R.color.colorLightGray))
                    v.invalidate()
                }

                MotionEvent.ACTION_UP -> {
                    v.setBackgroundColor(getColor(R.color.colorGray))
                    v.invalidate()
                    lonnFelt1.text = null
                    lonnFelt2.text = null
                    lonnFelt3.text = null
                    resultatFelt.text = null
                }
            }
            false

        }
    }
}