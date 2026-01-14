package com.example.pengelolaandatamotorshowroom.uicontroller.route

object DestinasiFormMotor : DestinasiNavigasi {
    override val route = "form_motor"
    const val BRAND_ID = "brandId"
    const val BRAND_NAME = "brandName"
    const val MOTOR_ID = "motorId"
    val routeWithArgs = "$route/{$BRAND_ID}/{$BRAND_NAME}?$MOTOR_ID={$MOTOR_ID}"

    fun createRouteAdd(brandId: Int, brandName: String) = "$route/$brandId/$brandName"
    fun createRouteEdit(brandId: Int, brandName: String, motorId: Int) = "$route/$brandId/$brandName?$MOTOR_ID=$motorId"
}