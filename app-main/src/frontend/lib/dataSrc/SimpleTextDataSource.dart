// Template file to demonstrate DI in Flutter
abstract class ISimpleTextDataSource {
  String getText();

  static ISimpleTextDataSource newInstance() => _SimpleTextDataSource();
}

class _SimpleTextDataSource implements ISimpleTextDataSource {
  @override
  String getText() {
    return "Hello, world";
  }
}
