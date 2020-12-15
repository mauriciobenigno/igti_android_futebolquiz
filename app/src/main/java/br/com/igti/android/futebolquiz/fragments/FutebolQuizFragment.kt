package br.com.igti.android.futebolquiz.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import br.com.igti.android.futebolquiz.Pergunta
import br.com.igti.android.futebolquiz.R
import br.com.igti.android.futebolquiz.model.AudioPlayer


class FutebolQuizFragment : Fragment() {

    val PREF_PRIMEIRA_VEZ : String = "primeiraVez"
    lateinit var prefs : SharedPreferences
    lateinit var audio : AudioPlayer

    private var mBotaoVerdade: Button? = null
    private var mBotaoFalso: Button? = null
    private var mConteudoCard: TextView? = null
    private var mCardView: CardView? = null

    private var mIndiceAtual = 0

    private val mPerguntas = arrayOf(
            Pergunta(R.string.cardview_conteudo_joinville, true),
            Pergunta(R.string.cardview_conteudo_cruzeiro, false),
            Pergunta(R.string.cardview_conteudo_gremio, false)
    )

    init {

    }

    override fun onResume() {
        super.onResume()
        // Carrega os prefs
        prefs = requireActivity().getSharedPreferences(PREF_PRIMEIRA_VEZ, AppCompatActivity.MODE_PRIVATE)
        val primeiraVez = prefs.getBoolean(PREF_PRIMEIRA_VEZ, true)

        // Saudação de boas vindas
        if (primeiraVez)
            Toast.makeText(context, "Bem Vindo ao FutebolQuiz!", Toast.LENGTH_SHORT).show()

        prefs.edit().putBoolean(PREF_PRIMEIRA_VEZ, true).apply()
        audio = AudioPlayer()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_futebol_quiz, container, false)

        // Binda a views em variáveis locais
        mBotaoVerdade = view.findViewById<View>(R.id.botaoVerdade) as Button
        mBotaoFalso = view.findViewById<View>(R.id.botaoFalso) as Button
        mConteudoCard = view.findViewById<View>(R.id.cardviewConteudo) as TextView
        mCardView = view.findViewById<View>(R.id.cardview) as CardView

        // Coloca observadores de click
        mBotaoVerdade!!.setOnClickListener(mBotaoVerdadeListener)
        mBotaoFalso!!.setOnClickListener(mBotaoFalsoListener)

        // Atualiza a questão inicial
        atualizaQuestao()

        return view
    }

    private val mBotaoVerdadeListener = View.OnClickListener {
        checaResposta(true)
        mIndiceAtual = (mIndiceAtual + 1) % mPerguntas.size
        atualizaQuestao()
        revelaCard()
    }

    private val mBotaoFalsoListener = View.OnClickListener {
        checaResposta(false)
        mIndiceAtual = (mIndiceAtual + 1) % mPerguntas.size
        atualizaQuestao()
        revelaCard()
    }

    private fun atualizaQuestao() {
        val questao = mPerguntas[mIndiceAtual].questao
        mConteudoCard!!.setText(questao)
    }

    private fun revelaCard() {
        //criando um reveal circular
        val animator = ViewAnimationUtils.createCircularReveal(
                mCardView,
                0,
                0, 0f,
                Math.hypot(mCardView!!.width.toDouble(), mCardView!!.height.toDouble()).toFloat())

        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.start()
    }

    private fun checaResposta(botaoPressionado: Boolean) {
        val resposta = mPerguntas[mIndiceAtual].isQuestaoVerdadeira
        var recursoRespostaId: String

        if (botaoPressionado == resposta) {
            recursoRespostaId =  getString(R.string.toast_acertou)
            acertou()
        } else {
            recursoRespostaId = getString(R.string.toast_errou)
            errou()
        }
        Toast.makeText(activity, recursoRespostaId, Toast.LENGTH_SHORT).show()
    }

    private fun acertou(){
        audio.play(requireContext(), R.raw.cashregister)
    }

    private fun errou(){
        audio.play(requireContext(), R.raw.buzzer)
        // Vibrar quando errar
        val vibrador = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if(vibrador.hasVibrator()){
            vibrador.vibrate(1000)
        }
    }
}