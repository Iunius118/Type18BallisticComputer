# Type 18 Gun Sight

Type 18 Gun Sight is a Minecraft modification which adds a lead-computing gun sight for Minecraft's projectiles.  
Type 18 Gun Sightは、Minecraftに見越角計算機能付きの照準器を追加するmodです。

## Download

- [**Releases**](https://github.com/Iunius118/Type18GunSight/releases)
  - v1.12.2-1.0.0.0 (requires Forge 1.12.2-1.12.2-14.23.5.2768+)

## Recipe

### Type 18 Gun Sight （18式照準器）

i = iron ingot（鉄インゴット）、G = Glass（ガラス）、P = Piston（ピストン）、c = clock（時計）

```text
iG
Pc
```

## How to Use

1. ゲーム内のmod一覧から`Type 18 Gun Sight`を選択し、`Config`画面を開いて使用する射撃武器と弾丸の各パラメータを設定する
    - このmodでは以下の物理モデルに従って弾丸の運動をシミュレートしている：

      ```text
      motion(1) = direction * initial_velocity
      motion(t) = motion(t - 1) * resistance_factor - {0, gravity_factor, 0}
      pos(t) = pos(t - 1) + motion(t)
      t 時点での当たり判定は pos(t - 1) → pos(t) の区間で行われる。
      ```

2. Type 18 Gun Sightをメインハンドに持って、モブやブロックを左クリックする
    - 対象に近すぎると反応しない（対象を殴ってしまう）ので、対象から6メートル以上離れて左クリックする
3. 画面に□と◇のマーカーが表示される
    - □マーカーは追尾しているモブやブロック。なお追尾可能な最大距離は256m
    - ◇マーカーは設定したパラメータと追尾対象の運動ベクトルから算出した見越し点。算出不能なときは表示されない
    - 各マーカーは一人称視点でType 18 Gun Sightをメインハンドまたはオフハンドに持っているときのみ表示される
4. 追尾を解除するときはType 18 Gun Sightをメインハンドに持って、空を左クリックするかスニーク状態でどこかを左クリックする

----

Copyright (c) 2019 Iunius118
