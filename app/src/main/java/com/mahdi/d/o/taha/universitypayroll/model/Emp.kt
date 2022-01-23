package com.mahdi.d.o.taha.universitypayroll.model

import android.os.Parcel
import android.os.Parcelable

data class Emp(
    val emp_name: String?,
    val emp_id: String?,
    val emp_contract_type: String?,
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
        val emp_accepted_holidays: String?,
        val emp_remaining_holidays: String?,
        val emp_salary: String?,
        val emp_date_work_day: String?,
        val emp_total_salaries: String?,
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readParcelable(Emp::class.java.classLoader)!!,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeParcelable(emp, flags)
            parcel.writeString(emp_accepted_holidays)
            parcel.writeString(emp_remaining_holidays)
            parcel.writeString(emp_salary)
            parcel.writeString(emp_date_work_day)
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
    data class Casual(val username:String, val id:String, val contract:String,val sal:String,val year:String,val payments:String):Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(username)
            parcel.writeString(id)
            parcel.writeString(contract)
            parcel.writeString(sal)
            parcel.writeString(year)
            parcel.writeString(payments)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Casual> {
            override fun createFromParcel(parcel: Parcel): Casual {
                return Casual(parcel)
            }

            override fun newArray(size: Int): Array<Casual?> {
                return arrayOfNulls(size)
            }
        }
    }

    data class Full(val username:String, val id:String, val contract:String,val sal:String,val year:String,val holidays:String,val remainingHolidays:String, val payments:String):Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(username)
            parcel.writeString(id)
            parcel.writeString(contract)
            parcel.writeString(sal)
            parcel.writeString(year)
            parcel.writeString(holidays)
            parcel.writeString(remainingHolidays)
            parcel.writeString(payments)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Full> {
            override fun createFromParcel(parcel: Parcel): Full {
                return Full(parcel)
            }

            override fun newArray(size: Int): Array<Full?> {
                return arrayOfNulls(size)
            }
        }
    }
}