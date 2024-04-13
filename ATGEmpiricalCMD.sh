echo "IC3"
echo "Processing dex file."

apk_path=$1
android_platforms=$2
result_dir=$3/ic3_dialdroid
working_dir=$(pwd)

apkname=$(basename $apk_path)

apk_path=$(realpath $apk_path)
android_platforms=$(realpath $android_platforms)
if [ ! -d "$result_dir" ]; then
	mkdir -p "$result_dir"
fi
result_dir=$(realpath $result_dir)
echo "Result dir: $result_dir"

echo "APK path: $apk_path"
echo "JRE path: $android_platforms"
echo "Apk Name: $apkname"
echo "Working dir: $working_dir"

tool_dir=$(dirname "$(readlink -f "$0")")
echo "tool dir: $tool_dir"
cd $tool_dir

START=$(date +%s)
java -jar build/ic3-dialdroid.jar -input $apk_path -cp $android_platforms -protobuf $result_dir > $result_dir/log.log
END=$(date +%s)
DIFF=$(( $END - $START ))
echo "Processed in $DIFF seconds."  >> $result_dir/ic3_dialdroid.log