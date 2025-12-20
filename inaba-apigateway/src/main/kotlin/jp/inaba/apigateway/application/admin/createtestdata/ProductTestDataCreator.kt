
@file:Suppress("ktlint:standard:max-line-length")

package jp.inaba.apigateway.application.admin.createtestdata

import de.huxhorn.sulky.ulid.ULID
import jp.inaba.grpc.product.CreateProductGrpc
import jp.inaba.grpc.product.CreateProductRequest
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Service

@Service
class ProductTestDataCreator(
    @GrpcClient("global")
    private val grpcService: CreateProductGrpc.CreateProductBlockingStub,
) {
    fun handle(brandId: String) {
        for (i in Product.DATA) {
            val grpcRequest =
                CreateProductRequest
                    .newBuilder()
                    .setId(ULID().nextULID())
                    .setBrandId(brandId)
                    .setName(i.name)
                    .setDescription(i.description)
                    .setPrice(i.price)
                    .build()

            grpcService.handle(grpcRequest)
        }
    }
}

data class Product(
    val name: String,
    val description: String,
    val image: String?,
    val price: Int,
) {
    companion object {
        val DATA =
            listOf(
                Product(
                    name = "TUF GAMING B860M-PLUS",
                    description = "LGA1851対応 intel B860チップセット搭載MicroATXマザーボード",
                    image = null,
                    price = 29480,
                ),
                Product(
                    name = "VG259QM",
                    description = "24.5インチ フルHD(1920x1080)、最大リフレッシュレート280Hz・応答速度1ms(GtG)対応 IPSパネル搭載ゲーミングモニター",
                    image = null,
                    price = 39770,
                ),
                Product(
                    name = "ROG STRIX 1200W Platinum",
                    description = "スタイリッシュなデザインに高効率を実現するGaN MOSFETとインテリジェントスタビライザーを採用し、安定した電力供給を実現する低温性と静音性に優れた電源ユニット",
                    image = null,
                    price = 51980,
                ),
                Product(
                    name = "DUAL-RTX4060-O8G-EVO",
                    description = "NVIDIA GeForce RTX 4060を搭載。セミファンレス仕様2.5スロット占有デュアルファンクーラーとバックプレートを採用するOC版グラフィックスカード",
                    image = null,
                    price = 97000,
                ),
                Product(
                    name = "TUF GAMING B550-PLUS",
                    description = "TUF Gaming B550-Plusは、最新のAMDプラットフォームの重要な要素を抽出し、ゲーム対応の機能と実証済みの耐久性と組み合わせたものです。 軍用グレードのコンポーネント、アップグレードされた電源ソリューション、包括的な冷却オプションのセットで設計されたこのマザーボードは、揺るぎないゲームの安定性と強固なパフォーマンスを提供します。",
                    image = null,
                    price = 14800,
                ),
                Product(
                    name = "PRIME Z790-A WIFI-CSM",
                    description = "ASUSスタンダードモデル「PRIME」シリーズ! 第13/12世代 intel Coreプロセッサ対応 intel Z790搭載ATXサイズ!DDR5メインメモリ、RGB LEDイルミネーション機能、PCIe5.0対応PCIe×16スロット、USB3.2 Gen2×2 Type-C、4つのPCIe4.0×4接続対応M.2スロット、intel 2.5GbE、Intel WiFi 6E & BT5.2等を搭載しています。",
                    image = null,
                    price = 42980,
                ),
                Product(
                    name = "PRIME B550M-K",
                    description = "ASUS Primeシリーズは、第3世代AMD Ryzenプラットフォームの潜在能力を最大限に引き出すために設計されています。Prime B550シリーズマザーボードは、堅牢な電源設計、包括的な冷却ソリューション、インテリジェントなチューニングオプションを誇り、日常的なユーザーやDIY PCビルダーに、直感的なソフトウェアとファームウェア機能を介して幅広いパフォーマンスチューニングオプションを提供します。",
                    image = null,
                    price = 13660,
                ),
                Product(
                    name = "PRIME H610M-A D4",
                    description = "ASUSスタンダードモデル「PRIME」シリーズ! 第12世代 intel Coreプロセッサ対応 intel H610搭載MicroATXサイズ!PCIe4.0対応PCIe×16スロット、PCIe3.0×4接続対応M.2スロット、PCIe3.0×2接続対応M.2スロット、intel GbE等を搭載しています。オンボードのRGB LEDヘッダに対応機器を接続しASUS Aura Syncでコントロール可能です。",
                    image = null,
                    price = 15090,
                ),
                Product(
                    name = "PRIME X670-P-CSM",
                    description = "ASUSスタンダードモデル「PRIME」シリーズ! AMD Ryzen 7000シリーズ対応、AMD X670 搭載ATXサイズ!DDR5メモリ対応、USB3.2 Gen2×2 Type-C、PCIe5.0×4接続対応M.2スロット、2つのPCIe4.0接続対応M.2スロット、Realtek 2.5GbE等を搭載しています。オンボードのRGB LEDヘッダに対応機器を接続しASUS Aura Syncでコントロール可能です。",
                    image = null,
                    price = 32120,
                ),
                Product(
                    name = "ROG STRIX LC III 360 ARGB LCD WHT",
                    description = "ROG Strix LC III 360 ARGB LCDで最高のパフォーマンスと息をのむような美しさを体験してください。最新のAsetek第7世代v2ポンプとROG ARGBラジエーターファンを搭載したこの液冷ソリューションは、AMD Ryzen™やインテル® Core™プロセッサーの熱に難なく対処し、最大限の力を引き出します。直感的なソフトウェアを使って液晶ディスプレイの角度を360度回転させ、お好みの向きに合わせることができます。",
                    image = null,
                    price = 45980,
                ),
                Product(
                    name = "ROG RYUO III 240 ARGB WHITE EDITION",
                    description = "最新のAsetek第8世代ポンプ採用、Anime Matrix™ディスプレイ搭載 240mm水冷クーラー",
                    image = null,
                    price = 19530,
                ),
                Product(
                    name = "ROG RYUJIN III WB",
                    description = "ROG Ryujin III WBは、最先端の冷却技術とカスタマイズ可能な個性的なデザインを見事に融合しました。178枚の小型の冷却フィンと銅合金製のコールドプレートを備えたAsetek社製水冷ブロックが抜群の熱効率を実現し、最新のインテル®およびAMD CPUの性能を最大限に引き出します。",
                    image = null,
                    price = 46970,
                ),
                Product(
                    name = "ROG Strix XG27UQ",
                    description = "超高解像度のストリーミング映像の画質を保ったまま一つのインターフェースで高速転送できるDisplay Stream Compression (DSC)技術を搭載。リフレッシュレート144Hzの高速駆動で非常に滑らかなゲーム映像を実現する27型IPSパネル4Kゲーミングモニター",
                    image = null,
                    price = 82280,
                ),
                Product(
                    name = "TUF Gaming GT502 Horizon",
                    description = "フロントと左サイドの2面に強化ガラスパネルを採用し、PCケース内部を美しく魅せることができるデュアルチャンバー構造のゲーミングPCケース",
                    image = null,
                    price = 29800,
                ),
                Product(
                    name = "TUF Gaming 750W Gold",
                    description = "新世代のATX3.0 / 16pinに対応可能",
                    image = null,
                    price = 19380,
                ),
                Product(
                    name = "TUF GAMING B650-E WIFI",
                    description = "ゲーム入門者向けASUSマザーボード「TUF GAMING」シリーズ。AMD Ryzen 8000G/7000シリーズ対応、AMD B650搭載ATXサイズ!DDR5メモリ対応、フロントUSB 20Gbps Type-C、PCIe5.0×4接続対応M.2スロット、2つのPCIe4.0×4接続対応M.2スロット、Realtek 2.5GbE、WiFi 6E & BT5.3等を搭載。オンボードのRGB LEDヘッダに対応機器を接続しASUS Aura Syncでコントロール可能です。",
                    image = null,
                    price = 19780,
                ),
                Product(
                    name = "ASUS NUC 13 Pro Kit NUC13ANKi7",
                    description = "ASUS NUC 13 Pro Kit Slim は、ビジネスやホームオフィスの幅広いニーズに対応します。",
                    image = null,
                    price = 115500,
                ),
                Product(
                    name = "ROG STRIX X870-I GAMING WIFI",
                    description = "ASUSゲーマー向けマザーボード「ROG STRIX GAMING」。AMD Ryzen 9000/8000/7000シリーズ対応、AMD X870搭載Mini-ITXサイズ。DDR5メモリ対応、DP alt Mode対応のUSB 40Gbps Type-Cを2ポート、PCIe5.0×4接続対応M.2スロット、PCIe4.0×4接続対応M.2スロット、intel 2.5GbE、WiFi 7 & BT5.4等を搭載。",
                    image = null,
                    price = 46970,
                ),
                Product(
                    name = "TUF Gaming ARGB PWM Fan Hub",
                    description = "最大6台のケースファンをPWMおよびARGB制御で接続し、冷却とライティング効果をシームレスに同期します。また、動作インジケーターにより、ファンの状態が一目で確認できます。",
                    image = null,
                    price = 2970,
                ),
            )
    }
}
