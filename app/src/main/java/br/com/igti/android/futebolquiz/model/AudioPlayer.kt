package br.com.igti.android.futebolquiz.model

import android.content.Context
import android.media.MediaPlayer


class AudioPlayer {
    private var mPlayer: MediaPlayer? = null
    fun stop() {
        if (mPlayer != null) {
            mPlayer!!.release()
            mPlayer = null
        }
    }

    fun play(c: Context?, res: Int) {
        // prevenção se o usuário clicar
        // enquanto já estiver um som rodando
        stop()

        // cria uma nova instância
        mPlayer = MediaPlayer.create(c, res)

        // se o som terminou, limpa o player
        mPlayer?.setOnCompletionListener {
            stop()
        }
        // Inicia o som
        mPlayer?.start()
    }
}