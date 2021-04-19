package net.gsantner.opoc.util

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.addTo(c: CompositeDisposable){
    c.add(this)
}
fun Disposable.remove(c:CompositeDisposable){
    c.remove(this)
}
