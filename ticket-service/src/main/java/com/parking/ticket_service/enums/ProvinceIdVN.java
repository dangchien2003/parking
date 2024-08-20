package com.parking.ticket_service.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public enum ProvinceIdVN {
    AN_GIANG("89", "An Giang"),
    BA_RIA_VUNG_TAU("77", "Bà Rịa - Vũng Tàu"),
    BAC_GIANG("24", "Bắc Giang"),
    BAC_KAN("06", "Bắc Kạn"),
    BAC_LIEU("95", "Bạc Liêu"),
    BAC_NINH("27", "Bắc Ninh"),
    BEN_TRE("83", "Bến Tre"),
    BINH_DUONG("74", "Bình Dương"),
    BINH_PHUOC("70", "Bình Phước"),
    BINH_THUAN("60", "Bình Thuận"),
    BINH_DINH("52", "Bình Định"),
    CA_MAU("96", "Cà Mau"),
    CAN_THO("92", "Cần Thơ"),
    CAO_BANG("04", "Cao Bằng"),
    GIA_LAI("64", "Gia Lai"),
    HA_GIANG("02", "Hà Giang"),
    HA_NAM("35", "Hà Nam"),
    HA_NOI("01", "Hà Nội"),
    HA_TINH("42", "Hà Tĩnh"),
    HAI_DUONG("30", "Hải Dương"),
    HAI_PHONG("31", "Hải Phòng"),
    HAU_GIANG("93", "Hậu Giang"),
    HO_CHI_MINH("79", "Hồ Chí Minh"),
    HOA_BINH("17", "Hoà Bình"),
    HUNG_YEN("33", "Hưng Yên"),
    KHANH_HOA("56", "Khánh Hòa"),
    KIEN_GIANG("91", "Kiên Giang"),
    KON_TUM("62", "Kon Tum"),
    LAI_CHAU("12", "Lai Châu"),
    LAM_DONG("68", "Lâm Đồng"),
    LANG_SON("20", "Lạng Sơn"),
    LAO_CAI("10", "Lào Cai"),
    LONG_AN("80", "Long An"),
    NAM_DINH("36", "Nam Định"),
    NGHE_AN("40", "Nghệ An"),
    NINH_BINH("37", "Ninh Bình"),
    NINH_THUAN("58", "Ninh Thuận"),
    PHU_THO("25", "Phú Thọ"),
    PHU_YEN("54", "Phú Yên"),
    QUANG_BINH("44", "Quảng Bình"),
    QUANG_NAM("49", "Quảng Nam"),
    QUANG_NGAI("51", "Quảng Ngãi"),
    QUANG_NINH("22", "Quảng Ninh"),
    QUANG_TRI("45", "Quảng Trị"),
    SOC_TRANG("94", "Sóc Trăng"),
    SON_LA("14", "Sơn La"),
    TAY_NINH("72", "Tây Ninh"),
    THAI_BINH("34", "Thái Bình"),
    THAI_NGUYEN("19", "Thái Nguyên"),
    THANH_HOA("38", "Thanh Hóa"),
    THUA_THIEN_HUE("46", "Thừa Thiên Huế"),
    TIEN_GIANG("82", "Tiền Giang"),
    TRA_VINH("84", "Trà Vinh"),
    TUYEN_QUANG("08", "Tuyên Quang"),
    VINH_LONG("86", "Vĩnh Long"),
    VINH_PHUC("26", "Vĩnh Phúc"),
    YEN_BAI("15", "Yên Bái"),
    DA_NANG("48", "Đà Nẵng"),
    DAK_LAK("66", "Đắk Lắk"),
    DAK_NONG("67", "Đắk Nông"),
    DIEN_BIEN("11", "Điện Biên"),
    DONG_NAI("75", "Đồng Nai"),
    DONG_THAP("87", "Đồng Tháp"),
    ;
    String id;

    String name;
}
