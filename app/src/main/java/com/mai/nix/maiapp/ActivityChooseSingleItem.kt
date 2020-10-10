package com.mai.nix.maiapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mai.nix.maiapp.choose_groups.ItemsAdapter
import kotlinx.android.synthetic.main.activity_choose_group_preference.*

class ActivityChooseSingleItem: AppCompatActivity(), ItemsAdapter.OnItemClickListener {

    companion object {
        private const val ITEMS = "items"
        const val ITEMS_RESULT = "com.mai.nix.maiapp.items_result"

        fun startActivity(context: AppCompatActivity, items: Array<String>, requestCode: Int) {
            val intent = Intent(context, ActivityChooseSingleItem::class.java)
            intent.putExtra(ITEMS, items)
            context.startActivityForResult(intent, requestCode)
        }
    }

    private val itemsAdapter = ItemsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_group_preference)
        itemsAdapter.callback = this
        val items = intent.getStringArrayExtra(ITEMS)
        prepareRecyclerView(items)
        background.setOnClickListener {
            finish()
        }
    }

    private fun prepareRecyclerView(items: Array<String>) {
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        linearLayoutManager.orientation = androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
        itemsList.layoutManager = linearLayoutManager
        itemsList.adapter = itemsAdapter
        val dividerItemDecoration = androidx.recyclerview.widget.DividerItemDecoration(this, linearLayoutManager.orientation)
        itemsList.addItemDecoration(dividerItemDecoration)
        itemsAdapter.items.addAll(items)
    }

    override fun itemClicked(position: Int) {
        val intent = Intent()
        intent.putExtra(ITEMS_RESULT, position)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}