package com.mohamednader.shoponthego.Profile.View

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mohamednader.shoponthego.Database.ConcreteLocalSource
import com.mohamednader.shoponthego.Home.View.Adapters.Coupons.CurrencyAdapter
import com.mohamednader.shoponthego.Home.View.Adapters.Coupons.OnCurrencyClickListener
import com.mohamednader.shoponthego.Model.Pojo.Currency.Currencies.CurrencyInfo
import com.mohamednader.shoponthego.Model.Repo.Repository
import com.mohamednader.shoponthego.Network.ApiClient
import com.mohamednader.shoponthego.Network.ApiState
import com.mohamednader.shoponthego.Profile.ViewModel.ProfileViewModel
import com.mohamednader.shoponthego.R
 import com.mohamednader.shoponthego.SharedPrefs.ConcreteSharedPrefsSource
import com.mohamednader.shoponthego.Utils.GenericViewModelFactory
import com.mohamednader.shoponthego.databinding.ActivitySettingsBinding
import com.mohamednader.shoponthego.databinding.BottomSheetDialogCurrenciesBinding
import com.mohamednader.shoponthego.databinding.FragmentHomeBinding
import com.mohamednader.shoponthego.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(), OnCurrencyClickListener {

    val TAG = "ProfileFragment_INFO_TAG"

    private lateinit var binding: FragmentProfileBinding

    //View Model Members
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var factory: GenericViewModelFactory

    //Currencies Bottom Sheet
    lateinit var currencyBottomSheetBinding: BottomSheetDialogCurrenciesBinding
    lateinit var currencyBottomSheetDialog: BottomSheetDialog
    private lateinit var currencyAdapter: CurrencyAdapter
    private lateinit var currencyLinearLayoutManager: LinearLayoutManager


    //Needed Variables
    lateinit var currenciesList: List<CurrencyInfo>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initViews()

    }


    private fun initViews() {

        //View Model
        factory = GenericViewModelFactory(
            Repository.getInstance(
                ApiClient.getInstance(),
                ConcreteLocalSource(requireContext()),
                ConcreteSharedPrefsSource(requireContext())
            )
        )
        profileViewModel = ViewModelProvider(this, factory).get(ProfileViewModel::class.java)

        //Currency Bottom Sheet
        currencyAdapter = CurrencyAdapter(requireContext(), this)
        currencyLinearLayoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        currencyBottomSheetBinding = BottomSheetDialogCurrenciesBinding.inflate(layoutInflater)
        currencyBottomSheetDialog = BottomSheetDialog(requireContext())
        currencyBottomSheetDialog.setContentView(currencyBottomSheetBinding.root)
        currencyBottomSheetBinding.currenciesRecyclerView.apply {
            adapter = currencyAdapter
            layoutManager = currencyLinearLayoutManager
        }


        binding.currency.setOnClickListener {
            currencyAdapter.submitList(currenciesList)
            currencyBottomSheetDialog.show()
        }

        apiRequests()

    }

    private fun apiRequests() {
        lifecycleScope.launchWhenStarted {

            launch {

                profileViewModel.currencyRes.collect { result ->
                    when (result) {
                        is ApiState.Success<List<CurrencyInfo>> -> {
                            Log.i(TAG, "onCreate: Success...{${result.data.get(0).iso}}")
                            currenciesList = result.data
                            currencyAdapter.submitList(currenciesList)
                        }
                        is ApiState.Loading -> {
                            Log.i(TAG, "onCreate: Loading...")
                        }
                        is ApiState.Failure -> {
                            //hideViews()
                            Toast.makeText(
                                requireContext(), "There Was An Error", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
        profileViewModel.getAllCurrenciesFromNetwork()
    }


    override fun onCurrencyClickListener(currencyISO: String) {
        Toast.makeText(requireContext(), "you Clicked $currencyISO", Toast.LENGTH_SHORT).show()
        currencyBottomSheetDialog.dismiss()
    }



}