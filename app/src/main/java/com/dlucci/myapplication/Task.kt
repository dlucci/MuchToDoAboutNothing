package com.dlucci.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(@PrimaryKey var item : String)