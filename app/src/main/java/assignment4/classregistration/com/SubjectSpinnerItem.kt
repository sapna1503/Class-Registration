package assignment4.classregistration.com

import java.io.Serializable

/**
 * Created by schandiramani on 3/31/18.
 */
class SubjectSpinnerItem : Serializable {

    var mSubject: Subject
    var mSelected: Boolean

    constructor(subject: Subject, checked: Boolean) {
        this.mSelected = checked
        this.mSubject = subject
    }
}