#
#
# build android apk and upload to fir

echo "丿--------build start--------"

flutter --version
flutter clean
flutter build apk --split-per-abi
echo "---------build success--------"

# create dir at root directory
mkdir -p "outputs/apk"
# copy file to root directory
mv "build/app/outputs/flutter-apk/app-armeabi-v7a-release.apk" "outputs/apk/release.apk"

# get apk path
apkPath="outputs/apk/release.apk"
# get icon path
iconPath="android/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png"

# 获取上传凭证
res=$(curl -X "POST" "http://api.bq04.com/apps" \
     -H "Content-Type: application/json" \
     -d "{\"type\":\"android\", \"bundle_id\":\"com.kevin.metro_app\", \"api_token\":\"13581c4350ed341b416a26cd998f7064\"}")

# icon key
key_icon=$(echo "$res" | jq -r '.cert.icon.key')
token_icon=$(echo "$res" | jq -r '.cert.icon.token')

# apk key
key_binary=$(echo "$res" | jq -r '.cert.binary.key')
token_binary=$(echo "$res" | jq -r '.cert.binary.token')

# upload apk file
function uploadApk() {
    echo "upload apk"
    uploadRes=$(curl   -F "key=$key_binary"     \
       -F "token=$token_binary"                 \
       -F "file=@$apkPath"                       \
       -F "x:name=metro_app"                    \
       -F "x:version=1.0.0"                     \
       -F "x:build=1"                           \
       https://up.qbox.me)
}

#upload app icon
function uploadIcon() {
    echo "upload icon"
    uploadRes=$(curl   -F "key=$key_icon" \
       -F "token=$token_icon"             \
       -F "file=@$iconPath"                \
       https://up.qbox.me)
}

uploadIcon
uploadApk

echo "---------upload finish--------"