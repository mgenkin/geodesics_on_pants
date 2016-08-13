cat ../../../pants/*.java > classes
sed -i -e 's/public class/class/g' classes
cat ../../../pants/pants.pde classes > pants_web.pde
rm classes