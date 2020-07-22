# Build Instructions
1. Copy api.properties.master over to api.properties
2. Add your consumer key and API url into api.properties where indicated
3. Build as per normal

## Additional Configuration
In order to test the general flow of the application without needing to load images,
the USE_MOCK_PHOTO_LOADER flag can be set to true, which will cause the app to instead
load deterministic pseudorandom (based on photo id) solid colour blocks instead of images.

Additionally, the build flags USE_MOCK_GALLERY_REPOSITORY and USE_MOCK_DETAIL_REPOSITORY can be
enabled in build.gradle. These two will cause the application to use a mocked out
fake repository for retrieving photo data. While the application will still function when the
live loader is paired with these mock repositories, it is recommended to use the mock loader.