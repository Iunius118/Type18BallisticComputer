# Type 18 Gun Sight

Type 18 Gun Sight is a Minecraft modification which adds a lead-computing gun sight for Minecraft's projectiles.

Type 18 Gun Sightは、Minecraftに見越角計算機能付きの照準器を追加するmodです。

## Downloads

- [**v1.14.4-1.0.0.0**](https://github.com/Iunius118/Type18GunSight/releases/download/v1.14.4-1.0.0.0/Type18GunSight-1.14.4-1.0.0.0.jar) (requires Forge 1.14.4-28.1.0+)
- [**v1.12.2-1.0.0.0**](https://github.com/Iunius118/Type18GunSight/releases/download/v1.12.2-1.0.0.0/Type18GunSight-1.12.2-1.0.0.0.jar) (requires Forge 1.12.2-1.12.2-14.23.5.2768+)

## Description

### Item

#### Type 18 Gun Sight （18式照準器）

i = iron ingot（鉄インゴット）、G = Glass（ガラス）、P = Piston（ピストン）、c = clock（時計）

```text
iG
Pc
```

### How to Use

1. 使用する射撃武器と弾丸に合わせて各パラメータを設定する
    - 各パラメータの詳細については後述
2. Type 18 Gun Sightをメインハンドに持って、モブやブロックを左クリックする
    - 対象に近すぎると反応しない（対象を殴ってしまう）ので、対象から6メートル以上離れて左クリックする
3. 画面に□と◇のマーカーが表示される
    - □マーカーは追尾しているモブやブロック。なお追尾可能な最大距離は256m
    - ◇マーカーは設定したパラメータと追尾対象の運動ベクトルから算出した見越し点。算出不能なときは表示されない
    - 各マーカーは一人称視点でType 18 Gun Sightをメインハンドまたはオフハンドに持っているときのみ表示される
    - なお、モブを追尾する場合は見越し点の算出にクライアント側の表示位置を使用しているため、ラグの大きいマルチプレイでは恐らく役に立たないと思われる
4. 追尾を解除するときはType 18 Gun Sightをメインハンドに持って、空を左クリックするかスニーク状態でどこかを左クリックする

## Configuration

- 1.14.4-xではゲームフォルダ内の`config`フォルダに生成される`type18gunsight-client.toml`をテキストエディタ等で開いて編集する。1.12.2-xではゲーム内のmod一覧から`Type 18 Gun Sight`を選択し、`Config`ボタンをクリックして表示した設定画面で設定する
- `initialVelocity`は弾丸の初速、`resistanceFactor`は弾丸の速度に毎tick乗算される減速（加速）率、`gravityFactor`は弾丸の垂直方向の速度から毎tick減算される重力値を表す
- `tickTimeToShoot`はクライアント側で発射ボタンが押されてからサーバー側で弾丸が発射されるまでの時間（プレイヤーの反応時間やボタン押下までの時間を含めて調整してもよい）、`maxFlightTick` は追尾モブの未来位置の予測を行う最大時間（あるいは弾丸に設定されている自滅までの最大飛行時間）を表す。従って追尾モブの未来位置の予測は、現時点より`tickTimeToShoot`後から最大で`tickTimeToShoot + maxFlightTick`後までの間で行われる。
- `marker_color`項目でマーカーの色が変更可能

このmodでは以下の物理モデルに従って弾丸の運動をシミュレートしている：

```text
弾丸がワールド内に生成されてからの経過時間を表す整数を t (t ≧ 0) とする。

motion(t) = { {0, 0, 0}    (t = 0)
            { shooting_direction * initialVelocity    (t = 1)
            { motion(t - 1) * resistanceFactor - {0, gravityFactor, 0}    (t > 1)

pos(t) = { shooter.eye_position    (t = 0)
        { pos(t - 1) + motion(t)    (t ≧ 1)

t 時点での当たり判定は pos(t - 1) → pos(t) の区間で行われる。
```

----

Copyright (c) 2019 Iunius118
