package uz.pdp.pedometr.listener

interface StepListener {
    fun step(timeNs: Long)
}