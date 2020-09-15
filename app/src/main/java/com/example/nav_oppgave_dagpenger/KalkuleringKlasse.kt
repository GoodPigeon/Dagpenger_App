package com.example.nav_oppgave_dagpenger

import android.provider.Settings.Global.getString
import android.util.Log
import java.lang.Exception
import kotlin.math.ceil
import kotlin.math.max

//Klasse for funksjon "kalkulerDagpenger" og Testing
class KalkuleringKlasse {

    //Konstante verdier
    val grunnbelop:Double = 101351.0 //Per 1. Mai 2020, kan automatiseres dersom man har tilgjengelig database
    val maxArbeidsdager:Double = 260.0

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
            return "0"
        }
        else if(!kvalifisert1 && !kvalifisert2){
            return "0"
        }

        //Dersom man har kvalifisert seg, vil dagsats regnes ut
        else{
            val dagpengegrunnlag1 = verdiAar1
            val dagpengegrunnlag2 = (verdiAar1+verdiAar2+verdiAar3)/3

            //Et grunnlag er større enn 6G, gir max dagpenger
            if(dagpengegrunnlag1 >= (6*grunnbelop) || dagpengegrunnlag2 >= (6*grunnbelop)){
                return(ceil((6*grunnbelop)/maxArbeidsdager)).toLong().toString()
            }
            //Ellers regnes dagpenger ut fra størst grunnlag
            else{
                val storstSats  = max(dagpengegrunnlag1, dagpengegrunnlag2)
                return(ceil(storstSats/maxArbeidsdager)).toLong().toString()
            }
        }
    }

    //Testfunksjon
    fun kjoerTest():String {
        try{
            //Utregning basert på siste året = 1924
            val test1 = kalkulerDagpenger(500000, 450000, 400000)
            if(test1 != "1924"){
                return("Feil i utregning 1!")
            }

            //Gjennomsnittlig utregning basert på tre siste år = 1411
            val test2 = kalkulerDagpenger(100000, 500000, 500000)
            if(test2 != "1411"){
                return("Feil i utregning 2!")
            }

            //Ikke kvalifisert = 0kr
            val test3 = kalkulerDagpenger(0, 1000000, 1000000)
            if(test3 != "0"){
                return("Feil i utregning 3!")
            }

            //Ikke kvalifisert = 0kr
            val test4 = kalkulerDagpenger(10,0,0)
            if(test4 != "0"){
                return("Feil i utregning 4!")
            }

            //max dagpenger = 2339kr
            val test5 = kalkulerDagpenger(10000000000, 10000000000, 10000000000)
            if(test5 != "2339"){
                return("Feil i utregning 5!")
            }
        }
        catch (ex: Exception){
            return "Ukjent feil dukket opp under testing"
        }
        return "Test kjoerte uten problemer"
    }


}