package de.frosner.rum

sealed trait Op
object InsertOp extends Op
object DeleteOp extends Op
