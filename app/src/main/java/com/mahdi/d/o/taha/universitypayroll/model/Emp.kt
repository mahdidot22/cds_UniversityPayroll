package com.mahdi.d.o.taha.universitypayroll.model

import android.os.Parcel
import android.os.Parcelable

data class Emp(
    val emp_name: String,
    val emp_id: String,
    val emp_contract_type: String,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    )

    companion object CREATOR : Parcelable.Creator<Emp> {
        override fun createFromParcel(parcel: Parcel): Emp {
            return Emp(parcel)
        }

        override fun newArray(size: Int): Array<Emp?> {
            return arrayOfNulls(size)
        }
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeString(emp_name)
        dest.writeString(emp_id)
        dest.writeString(emp_contract_type)
    }

    data class EpmJopInfo(
        val emp: Emp,
        val emp_accepted_holidays: String,
        val emp_remaining_holidays: String,
        val emp_salary: String,
        val emp_payment_date: String,
        val emp_payment_type: String,
        val emp_total_work_days: String,
        val emp_total_salaries: String,
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readParcelable(Emp::class.java.classLoader)!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeParcelable(emp, flags)
            parcel.writeString(emp_accepted_holidays)
            parcel.writeString(emp_remaining_holidays)
            parcel.writeString(emp_salary)
            parcel.writeString(emp_payment_date)
            parcel.writeString(emp_payment_type)
            parcel.writeString(emp_total_work_days)
            parcel.writeString(emp_total_salaries)
        }

        override fun describeContents(): Int = 0

        companion object CREATOR : Parcelable.Creator<EpmJopInfo> {
            override fun createFromParcel(parcel: Parcel): EpmJopInfo {
                return EpmJopInfo(parcel)
            }

            override fun newArray(size: Int): Array<EpmJopInfo?> {
                return arrayOfNulls(size)
            }
        }

    }
}