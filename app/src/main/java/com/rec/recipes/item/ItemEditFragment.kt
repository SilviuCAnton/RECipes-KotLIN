package com.rec.recipes.item

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_item_edit.*
import com.rec.R
import com.rec.core.TAG
import com.rec.recipes.data.Item

class ItemEditFragment : Fragment() {
    companion object {
        const val ITEM_ID = "ITEM_ID"
    }

    private lateinit var viewModel: ItemEditViewModel
    private var itemId: String? = null
    private var item: Item? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate")
        arguments?.let {
            if (it.containsKey(ITEM_ID)) {
                itemId = it.getString(ITEM_ID).toString()
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v(TAG, "onCreateView")
        return inflater.inflate(R.layout.fragment_item_edit, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated")
        setupViewModel()
        fab.setOnClickListener {
            Log.v(TAG, "save item")
            val i = item
            if (i != null) {
                i.name = item_name.text.toString()
                i.description = item_description.text.toString()
                i.calories = Integer.parseInt(item_calories.text.toString())
                viewModel.saveOrUpdateItem(i)
            }
        }

    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(ItemEditViewModel::class.java)
        viewModel.fetchingError.observe(viewLifecycleOwner) { exception ->
            if (exception != null) {
                Log.v(TAG, "update fetching error")
                val message = "Fetching exception ${exception.message}"
                val parentActivity = activity?.parent
                if (parentActivity != null) {
                    Toast.makeText(parentActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.completed.observe(viewLifecycleOwner) { completed ->
            if (completed) {
                Log.v(TAG, "completed, navigate back")
                findNavController().popBackStack()
            }
        }
        val id = itemId
        if (id == null) {
            item = Item("", "", "", 0, "")
        } else {
            viewModel.getItemById(id).observe(viewLifecycleOwner) {
                Log.v(TAG, "update items")
                item = it
                item_name.setText(it.name)
                item_description.setText(it.description)
                item_calories.setText(it.calories.toString())
            }
        }
    }
}
