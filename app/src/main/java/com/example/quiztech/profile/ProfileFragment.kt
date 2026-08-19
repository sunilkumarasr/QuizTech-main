package com.example.quiztech.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.quiztech.auth.LoginActivity
import com.example.quiztech.databinding.FragmentProfileBinding
import com.prvt.sreezzyuser.common.Utils

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    var name=""
    var email=""
    var phone=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        name= Utils.getData(requireContext(),"name","").toString()
        email= Utils.getData(requireContext(),"email","").toString()
        phone= Utils.getData(requireContext(),"phone","").toString()

       binding.txtName.setText(name)
       binding.txtEmail.setText(email)
       binding.txtPhone.setText(phone)
        binding.tvEdit.setOnClickListener {
            // Handle Edit Profile click
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }

        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            // Handle notification switch state change
            val status = if (isChecked) "ON" else "OFF"
            Toast.makeText(requireContext(), "Notifications are $status", Toast.LENGTH_SHORT).show()
        }

        binding.llMySubscriptions.setOnClickListener {
            startActivity(Intent(requireContext(), MySubscriptionActivity::class.java))
        }

        binding.llAboutUs.setOnClickListener {
            // Handle About Us click
            val intent = Intent(requireContext(), WebViewActivity::class.java).apply {
                putExtra("title", "About Us")
                putExtra("url", "about-us")
                putExtra("isUrl", true)
            }
            startActivity(intent)
        }

        binding.llPrivacyPolicy.setOnClickListener {
            // Handle Privacy Policy click
            val intent = Intent(requireContext(), WebViewActivity::class.java).apply {
                putExtra("title", "Privacy Policy")
                putExtra("url", "privacy-policy")
                putExtra("isUrl", true)
            }
            startActivity(intent)
        }

        binding.llTermsAndConditions.setOnClickListener {
            // Handle Terms & Conditions click
            val intent = Intent(requireContext(), WebViewActivity::class.java).apply {
                putExtra("title", "Terms & Conditions")
                putExtra("url", "terms-and-conditions")
                putExtra("isUrl", true)
            }
            startActivity(intent)
        }

        binding.llContactUs.setOnClickListener {
            // Handle Contact Us click
            startActivity(Intent(requireContext(), ContactUsActivity::class.java))
        }

        binding.llFaqs.setOnClickListener {
            // Handle FAQ's click
            startActivity(Intent(requireContext(), FaqActivity::class.java))
        }

        binding.llEnquiryForm.setOnClickListener {
            // Handle Enquiry Form click
            startActivity(Intent(requireContext(), EnquiryFormActivity::class.java))
        }

        binding.llShareApp.setOnClickListener {
            shareApp()
        }

        binding.llLogout.setOnClickListener {
            Utils.clearPref(requireContext())
            var intent=Intent(requireContext(), LoginActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            // Handle Logout click
        }
    }

    private fun shareApp() {
        val packageName = requireContext().packageName
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "Check out the Quiz Tech app: https://play.google.com/store/apps/details?id=$packageName"
            )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}