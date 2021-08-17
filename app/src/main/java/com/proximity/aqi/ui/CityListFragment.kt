package com.proximity.aqi.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.baruckis.cryptolive.database.CityNameEntity
import com.proximity.aqi.adapter.CityListAdapter
import com.proximity.aqi.databinding.MainFragmentBinding
import com.proximity.aqi.ui.viewmodel.DataViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class CityListFragment : Fragment() {

    companion object {
        fun newInstance() = CityListFragment()
    }

    private val viewModel: DataViewModel by viewModels()
    private lateinit var binding: MainFragmentBinding
    private val adapter = CityListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.list.layoutManager = LinearLayoutManager(view.context)
        binding.list.adapter = adapter
        activity?.let { adapter.setContext(it) }

        viewModel.cityNameData?.observe(
            viewLifecycleOwner, {
                adapter.resultsList = it as ArrayList<CityNameEntity>
            })
    }
}