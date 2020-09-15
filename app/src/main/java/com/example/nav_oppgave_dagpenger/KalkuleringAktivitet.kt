package com.example.nav_oppgave_dagpenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.text.set
import java.util.*
import kotlin.math.max

class KalkuleringAktivitet : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.aktivitet_kalkulering)

        //Konstante verdier
        val grunnbelop = 101351 //Per 1. Mai 2020, kan automatiseres dersom man har tilgjengelig database
        val maxArbeidsdager = 260

        //Design-elementer
        val aarfelt1 = findViewById<TextView>(R.id.aarfelt1)
        val aarfelt2 = findViewById<TextView>(R.id.aarfelt2)
        val aarfelt3 = findViewById<TextView>(R.id.aarfelt3)
        val lonnFelt1 = findViewById<EditText>(R.id.lonnfelt1)
        val lonnFelt2 = findViewById<EditText>(R.id.lonnfelt2)
        val lonnFelt3 = findViewById<EditText>(R.id.lonnfelt3)
        val kalkulerKnapp = findViewById<Button>(R.id.kalkulerKnapp)
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

            if(!kvalifisert1 && !kvalifisert2 && !kvalifisert3){
                return(getString(R.string.ikkeKrav))
            }
            //Dersom man har tjent nok, vil dagsats regnes ut
            else{
                val dagpengegrunnlag1 = verdiAar1
                val dagpengegrunnlag2 = (verdiAar1+verdiAar2+verdiAar3)/3

                //Et grunnlag er større enn 6G, gir max dagpenger
                if(dagpengegrunnlag1 >= (6*grunnbelop) || dagpengegrunnlag2 >= (6*grunnbelop)){
                    return("kr " + ((6*grunnbelop)/maxArbeidsdager).toString())
                }
                //Ellers regnes dagpenger ut fra størst grunnlag
                else{
                    val storstSats = max(dagpengegrunnlag1, dagpengegrunnlag2)
                    return("kr " + (storstSats/maxArbeidsdager).toString())
                }
            }
        }

        //Kalkuleringsknappen skal regne ut potensielle dagpenger og fremstille resultatet i feltet under
        kalkulerKnapp.setOnClickListener {
            if (lonnFelt1.text.toString() == ""){
                lonnFelt1.setText("0")
            }
            if (lonnFelt2.text.toString() == ""){
                lonnFelt2.setText("0")
            }
            if (lonnFelt3.text.toString() == ""){
                lonnFelt3.setText("0")
            }
            else {
                val verdi1 = lonnFelt1.text.toString().toLong()
                val verdi2 = lonnFelt2.text.toString().toLong()
                val verdi3 = lonnFelt3.text.toString().toLong()
                val resultat = kalkulerDagpenger(verdi1, verdi2, verdi3)
                resultatFelt.setText(resultat)
            }
        }

        //TODO: Testing



    }
}