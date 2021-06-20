#import "FaceRecognitionPlugin.h"
#if __has_include(<face_recognition/face_recognition-Swift.h>)
#import <face_recognition/face_recognition-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "face_recognition-Swift.h"
#endif

@implementation FaceRecognitionPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFaceRecognitionPlugin registerWithRegistrar:registrar];
}
@end
