package com.mai.nix.maiapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.mai.nix.maiapp.choose_groups.ItemsAdapter
import kotlinx.android.synthetic.main.activity_choose_group_preference.*

class ActivityChooseGroupPreferences: AppCompatActivity(), ItemsAdapter.OnItemClickListener {

    companion object {
        private const val ITEMS = "items"
        const val ITEMS_RESULT = "com.mai.nix.maiapp.items_result"

        fun startActivity(context: AppCompatActivity, items: Array<String>, requestCode: Int) {
            val intent = Intent(context, ActivityChooseGroupPreferences::class.java)
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
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        itemsList.layoutManager = linearLayoutManager
        itemsList.adapter = itemsAdapter
        val dividerItemDecoration = DividerItemDecoration(this, linearLayoutManager.orientation)
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