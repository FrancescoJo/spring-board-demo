// Template file to demonstrate DI in Flutter
// Not actually a view model, we'll transform this to real view model later
// https://medium.com/better-programming/mvvm-in-flutter-edd212fd767a
import 'package:frontend/repository/SimpleTextRepository.dart';

class MyAppViewModel {
  final ISimpleTextRepository _repo;

  MyAppViewModel(this._repo);

  String getText() {
    return _repo.getText();
  }
}
