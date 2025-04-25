Proje Adı: Instagram Clone

Proje Tanımı: Instagram Clone uygulamamız, kullanıcıların fotoğraf ve video paylaşımlarını gerçekleştirebileceği, başkalarını takip edebileceği, beğenip yorum yapabileceği, aynı zamanda profil bilgilerini düzenleyebileceği ve hikayeler paylaşabileceği sosyal bir platformdur. Kullanıcılar, diğer kullanıcıları takip ederek onların paylaşımlarını görüntüleyebilir, kendi içeriklerini paylaşabilir ve etkileşimde bulunabilirler. Uygulama, modern ve kullanıcı dostu arayüzü ile sosyal medya deneyimini mobil cihazlarda kolay ve keyifli hale getirmektedir. Firebase ile verilerin güvenli bir şekilde saklanması sağlanarak, kullanıcıların kişisel bilgileri ve paylaşımları güvende tutulur. Instagram Clone, sosyal medya etkileşimlerini her an, her yerde gerçekleştirmenizi sağlar.

Proje Kategorisi: Sosyal Medya

Referans Uygulama: Instagram

Uygulama Adresi: yok

Grup Adı: KaragunluAhmet

1-[Gereksinim Analizi](https://github.com/Ahmetkaragunlu/InstagramClone/blob/main/Gereksinim-Analizi.md)


2-[Durum Senaryoları](https://github.com/Ahmetkaragunlu/InstagramClone/blob/main/AhmetKaragunlu-DurumSenaryolari.pdf)


A modern Android application built with Jetpack Compose, allowing users to update their profile picture, share posts, like and comment on other posts. The app follows a clean architecture using MVVM, Firebase integration, and Dependency Injection via Hilt.

🔧 Technologies Used
Jetpack Compose – Declarative and modern UI toolkit for Android

Firebase Authentication – User authentication and session management

Firebase Firestore – Real-time NoSQL cloud database

Firebase Storage – Cloud storage for images and media files

Hilt (Dependency Injection) – Simplifies dependency injection in Android

MVVM Architecture – Separation of concerns and scalable code structure

Kotlin Coroutines & Flow – For asynchronous and reactive programming

🧩 How It Works
Profile Picture Update: Users can select an image from their gallery and upload it to Firebase Storage as their profile picture.

Post Creation: Users can share posts that include an image and a description, which are saved in Firestore.

Like & Comment: Other users can like and comment on posts in real-time.

Reactive UI: Jetpack Compose listens to state changes and automatically updates the UI.

Seamless Integration: The app integrates Firebase services with ViewModel and Repository layers, using Hilt for managing dependencies cleanly.
