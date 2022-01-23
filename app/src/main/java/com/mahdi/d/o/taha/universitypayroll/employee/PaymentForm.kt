package com.mahdi.d.o.taha.universitypayroll.employee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mahdi.d.o.taha.universitypayroll.databinding.ActivityPaymentFormBinding
import com.mahdi.d.o.taha.universitypayroll.model.Constants
import com.mahdi.d.o.taha.universitypayroll.model.Emp

class PaymentForm : AppCompatActivity() {
    private var _binding: ActivityPaymentFormBinding? = null
    private val binding get() = _binding!!
    private var _constants: Constants? = null
    private val constants get() = _constants!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPaymentFormBinding.inflate(layoutInflater)
        _constants = Constants()
        setContentView(binding.root)
        val type = intent.getStringExtra("type")
        if (type == "Part-Time") {
            val list = intent.getParcelableArrayListExtra<Emp.Casual>("user")
            binding.apply {
                edHolidays.visibility = View.VISIBLE
                btnPayment.setOnClickListener {
                    if (edHolidays.text.isNotEmpty()) {
                        constants.fillFields(binding)
                    } else {
                        when (paymentMethod.checkedRadioButtonId) {
                            check.id -> {
                                startActivity(
                                    Intent(
                                        this@PaymentForm,
                                        CheckActivity::class.java
                                    ).putExtra("user", list)
                                )
                            }
                            credit.id -> {
                                startActivity(Intent(this@PaymentForm, CreditCardForm::class.java))
                            }
                            bank.id -> {
                                startActivity(
                                    Intent(
                                        this@PaymentForm,
                                        BankActivity::class.java
                                    ).putExtra("user", list)
                                )
                            }
                        }

                    }
                }
            }
        } else {
            val list = intent.getParcelableArrayListExtra<Emp.Full>("user")
            binding.apply {
                edHolidays.visibility = View.GONE
                btnPayment.setOnClickListener {
                    when (paymentMethod.checkedRadioButtonId) {
                        check.id -> {
                            startActivity(
                                Intent(
                                    this@PaymentForm,
                                    CheckActivity::class.java
                                ).putExtra("user", list).putExtra("type",type)
                            )
                        }
                        credit.id -> {
                            startActivity(Intent(this@PaymentForm, CreditCardForm::class.java))
                        }
                        bank.id -> {
                            startActivity(
                                Intent(
                                    this@PaymentForm,
                                    BankActivity::class.java
                                ).putExtra("user", list).putExtra("type",type)
                            )
                        }
                    }
                }
            }
        }
    }
}