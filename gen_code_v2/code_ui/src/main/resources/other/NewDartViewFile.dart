import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class ${NAME} extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return new _${NAME}State();
  }
}

class _${NAME}State extends State< ${NAME}> {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return Scaffold(
      appBar: AppBar(
        title: Text("首页"),
      ),
      body: Center(
        child: IconButton(
            icon: Icon(Icons.add),
            onPressed: () {
            //  Navigator.pushNamed(context, "/login");
            }),
      ),
    );
  }
}
