# Handoff: Student Management Labs (ต่อจาก Lab 3)

เอกสารนี้ใช้เริ่มแชทใหม่เพื่อทำแล็บที่เหลือ อ่านให้ครบก่อนลงมือ

## 0. กติกาที่ห้ามละเมิด (ผู้ใช้กำหนดเอง เน้นย้ำหลายครั้ง)

1. **ยึดชีทอาจารย์ใน `doc/` เป็นหลักเสมอ** อ่านชีทของแล็บนั้นใหม่ทุกครั้งก่อนลงมือ ใช้ตำแหน่งไฟล์ ชื่อไฟล์ ชื่อคลาส และรูปแบบโค้ดตามชีท
2. **ห้ามทำเกินชีทเด็ดขาด** ห้ามเพิ่มฟีเจอร์ ปุ่ม การจัดการ error หรือรีแฟคเตอร์ที่ชีทไม่ได้สั่ง แม้จะมีประโยชน์ ถ้าคิดว่าจำเป็นต้องเพิ่ม (เช่น ชีทขาดส่วนที่ทำให้ระบบใช้ไม่ได้) **ให้ถามผู้ใช้ก่อน** ห้ามตัดสินใจเอง
3. **ห้ามเขียน comment ในโค้ดเอง** (comment ที่มีอยู่ในโค้ดตัวอย่างของชีทใส่ได้ถ้าชีทมี)
4. **ห้ามรัน `git add`, `git commit`, `git tag`, `git push`** ผู้ใช้ทำเอง สร้าง branch ได้เมื่อได้รับคำสั่ง เมื่อจบแล็บให้บอกว่าพร้อม commit และเสนอข้อความ commit ได้
5. ทำเฉพาะแล็บที่กำลังทำ ห้ามย้ายหรือเตรียมไฟล์ของแล็บถัดไปล่วงหน้า
6. ตอบเป็นภาษาไทย อธิบายสั้น ๆ ว่าแก้อะไรและทำไม (ผู้ใช้ต้องการเข้าใจบทเรียน แต่เวลาน้อย จึงยอมให้เขียนโค้ดแทนได้)
7. ห้ามแสดงหรืออ่านค่าใน `student-api/.env` (มีรหัส DB และ JWT secret) ถ้าต้องดูให้ดูแค่ชื่อตัวแปร (`cut -d= -f1`)

## 1. ข้อมูลโปรเจกต์

| เรื่อง | ค่า |
|---|---|
| Repo | `D:\Documents\GitHub\StudentManagement` (remote: `github.com/FelixTEK/StudentManagement`) |
| Package | `com.example.studentmanagement` (ชีทเขียน `studentapp` **ให้คงชื่อเดิม** ห้ามเปลี่ยน) |
| Android | Kotlin 2.2.10, AGP 8.8.2, Gradle 9.5.0, KSP 2.2.10-2.0.2 (Room ใช้ **KSP** ไม่ใช่ kapt), compileSdk/targetSdk 35, minSdk 24, Compose BOM 2026.02.01, Room 2.7.2, Retrofit 2.11.0 |
| Backend | `student-api/` FastAPI + SQLAlchemy + PostgreSQL 17 (`student_db`), JWT ด้วย `pyjwt` + `bcrypt` |
| เครื่องทดสอบ | **มือถือจริง** Samsung SM-A226B (Android 13, API 33) ใช้ Emulator ไม่ได้ (ชนกับ VirtualBox/VMware) |
| OS | Windows 11, มี PowerShell และ Git Bash |

### การรันระบบ

```bash
# backend (Git Bash ใน student-api/)
source venv/Scripts/activate
uvicorn main:app --reload
```
```powershell
# ทุกครั้งที่เสียบสายใหม่ หรือ adb/Android Studio รีสตาร์ต
& "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe" reverse tcp:8000 tcp:8000
```
- `BASE_URL = "http://127.0.0.1:8000/"` (ชีทใช้ `10.0.2.2` สำหรับ Emulator แต่เราใช้มือถือ + `adb reverse`) ใช้ค่านี้ในทุกที่ที่ชีทเขียน `10.0.2.2` รวมถึง `NetworkModule` ใน Lab 6
- ถ้าแอปขึ้น "เชื่อมต่อไม่ได้" ให้ตรวจ `adb reverse --list` ก่อนเป็นอย่างแรก
- ตรวจ build: `.\gradlew.bat assembleDebug` / ติดตั้ง: `.\gradlew.bat installDebug` (ถ้ามือถือไม่ได้เสียบจะ fail ที่ขั้น install แต่ยืนยันว่าคอมไพล์ผ่านได้)
- ถ้าผลบนมือถือแปลก ให้เทียบ `adb shell dumpsys package com.example.studentmanagement | grep lastUpdateTime` กับเวลา APK ล่าสุด (เคยเจอแอปบนเครื่องเป็นรุ่นเก่า)
- `student-api/.env` มีตัวแปร: `DATABASE_URL`, `JWT_SECRET`, `ACCESS_TOKEN_EXPIRE_MINUTES`, `SEED_USERNAME`, `SEED_PASSWORD` (ผู้ใช้ทดสอบสร้างด้วย `python seed_user.py`)

## 2. สถานะปัจจุบัน

| Branch | สถานะ |
|---|---|
| `lab2-part1` | เสร็จ |
| `lab2-part2` | เสร็จ (tag `lab2-part2-submit`) |
| `lab3` | **เสร็จ และผู้ใช้ commit แล้ว** (`62d17e5 Submit Assignment`) มีภาพส่งงานใน `submission/` |

กลยุทธ์ branch: แต่ละแล็บแตกจากแล็บก่อนหน้า (`git switch -c lab4 lab3` เป็นต้น)

### โครงสร้างโค้ด Android ตอนนี้ (`app/src/main/java/com/example/studentmanagement/`)

```
MainActivity.kt                 สร้าง dependency แบบ manual: Room("student_database"), TokenStorage,
                                RetrofitClient.create(tokenStorage), StudentApi, AuthApi, Repository,
                                StudentViewModel, LoginViewModel; ไม่มี token → LoginScreen, มี → StudentScreen
data/local/AppDatabase.kt       version = 1, exportSchema = false, entities = [Student]
data/local/StudentDao.kt        getAllStudents, insertStudent(REPLACE), deleteStudent, deleteAll, insertAll
data/remote/StudentApi.kt       GET/POST/PUT/DELETE students
data/remote/StudentDto.kt       id, studentCode (@SerializedName "student_code"), name, major
data/remote/StudentCreateRequest.kt  studentCode (@SerializedName "student_code"), name, major
data/remote/RetrofitClient.kt   object, fun create(tokenStorage): Retrofit (OkHttp + AuthInterceptor)
data/remote/AuthApi.kt, AuthInterceptor.kt, TokenResponse.kt (access_token, token_type), TokenStorage.kt
data/repository/StudentRepository.kt  getStudents(): API → deleteAll + insertAll ลง Room, ล้มเหลว → อ่าน Room;
                                add/update/delete เรียก API ก่อนแล้วค่อยเขียน Room
model/Student.kt                **Room Entity** @Entity("students"), @PrimaryKey id: Int, studentCode, name, major
ui/StudentScreen.kt             รายการ + Search + ปุ่ม Refresh / + Add Student / Delete, หน้า Detail + Edit dialog,
                                LaunchedEffect โหลดตอนเปิด
ui/login/LoginScreen.kt, LoginViewModel.kt   ตามชีท Lab 3
viewmodel/StudentViewModel.kt   mutableStateOf: students, isLoading, errorMessage, searchQuery, filteredStudents;
                                loadStudents, addStudent(code,name,major), updateStudent, deleteStudent
```

Backend (`student-api/`): `database.py` (อ่าน `DATABASE_URL`), `models.py` (Student, User), `schemas.py` (StudentCreate/Response, Token), `auth.py` (bcrypt, JWT, `get_current_user`), `main.py` (`POST /token`, `/students*` ทุก route มี `dependencies=[Depends(get_current_user)]`), `seed_user.py`, `README.MD` (ผู้ใช้เขียนเอง เรื่อง IP)

## 3. การวิเคราะห์ความต่อเนื่องของแล็บ

### ชีทใหม่: Lab 10 และ Lab 11

- **`Lab 10.pdf` เนื้อหาเหมือน `Lab 8.pdf`** (Multimedia and Sensors) ต่างแค่การจัดหน้า และระบุงานเพิ่มชัดขึ้นด้วย `***` (เพิ่ม Filter, เป้าหมาย 10,000 ก้าว)
- **`Lab 11.pdf` เนื้อหาเหมือน `Lab 9.pdf`** (Animation and Motion) เพิ่มหัวข้อ "Project Challenge" และระบุงานเปลี่ยน Loading → Walking ด้วย `***`
- Lab 9/11 เขียนว่า "เปิด Project จาก Week 10" แปลว่าอาจารย์ **เปลี่ยนเลขแล็บจาก 8, 9 เป็น 10, 11** (ไฟล์ 10/11 ใหม่กว่า ลงวันที่ 20 ต.ค.)
- **ต้องถามผู้ใช้ก่อนเริ่มแล็บ Multimedia:** ใช้ชีท Lab 10/11 แทน Lab 8/9 ใช่ไหม และจะตั้งชื่อ branch เป็น `lab10`/`lab11` หรือ `lab8`/`lab9` และในช่วงระหว่าง Lab 7 กับ Lab 10 ยังมี Lab 8/9 อื่นหรือไม่ (ถ้าอาจารย์จะออกชีท Lab 8, 9 ใหม่ ห้ามเดาเนื้อหา)

### ลำดับที่ต้องทำ (ต่อเนื่องเป็นเส้นเดียว)

```
lab3 (เสร็จ) → lab4 → lab5 → lab6 → lab7 → lab10 (หรือ lab8) → lab11 (หรือ lab9)
```

| แล็บ | ใช้ของจากแล็บก่อน | เปลี่ยนสถาปัตยกรรม |
|---|---|---|
| 4 Offline-First | Entity/DAO/API/Repository ของ Part 2 | Room เป็นแหล่งข้อมูลหลัก (Flow), เพิ่ม syncStatus |
| 5 WorkManager | `syncPending()` ของ Lab 4, `TokenStorage` ของ Lab 3 | เพิ่ม `AppContainer` + `StudentApplication` (manual DI) |
| 6 Hilt | ทุกอย่างจาก 3–5 | Hilt แทน `AppContainer` และแทน `RetrofitClient.create` |
| 7 Clean Architecture | Hilt (Lab 6), sync logic (4–5) | แยก domain/data/presentation, Use Case, Unit Test |
| 10 Multimedia | โปรเจกต์เดิม (ชีทบอกห้ามสร้างใหม่) | เพิ่มหน้าจอ Camera/Music/Video/Step + Home + Navigation |
| 11 Animation | `HomeScreen` และ Step Counter ของ Lab 10 | Animation ใน Home, Lottie, Screen Transition |

`การรวม Lab 3_7.pdf` คือภาพรวมระบบเมื่อทำครบ 3–7 (Login/JWT → Presentation → Use Case → Repository Interface → Room/Retrofit, Background Sync: WorkManager → SyncWorker → Sync Use Case → Repository) ใช้เป็นเป้าหมายตรวจทาน **ไม่ใช่ใบสั่งงานเพิ่ม** (เช่น Logout ที่อยู่ในไฟล์นี้ ผู้ใช้ตัดสินแล้วว่าไม่ทำใน Lab 3 เพราะชีท Lab 3 ไม่ได้สั่ง)

## 4. รายละเอียดแต่ละแล็บ + จุดที่ต้องถามผู้ใช้ก่อน

### Lab 4: Offline-First + Synchronization (`doc/LAB 4.pdf`, 7 ขั้น)

ตามชีท:
1. `data/local/StudentEntity.kt`: `localId` (PK autoGenerate), `serverId: Int?`, `studentCode`, `name`, `major`, `syncStatus: String = "PENDING"`, `updatedAt: Long` สถานะ PENDING/SYNCED/FAILED ชีทบอก "ถ้ามี Entity อยู่แล้วให้เพิ่มลงใน Entity เดิม ห้ามสร้างซ้ำ"
2. `StudentDao`: `observeStudents(): Flow<List<StudentEntity>>` (ORDER BY name), `getPendingStudents()`, `insertStudent` (มีแล้ว ใช้ของเดิม), `updateStudent` (@Update)
3. `StudentApi`: GET + POST มีอยู่แล้ว ชีทใช้ชื่อ `StudentRequest` ส่วนเรามี `StudentCreateRequest` (มี @SerializedName ตามที่ชีทเตือนไว้แล้ว)
4. `StudentRepository`: `observeStudents()`, `addStudent(student)` (บันทึก PENDING), `syncPending()` (POST ทีละรายการ → `serverId` + SYNCED)
5. `ui/student/StudentViewModel.kt`: `students = repository.observeStudents().stateIn(...)`, `addStudent(StudentEntity)`, `syncStudents()`
6. `ui/student/StudentScreen.kt`: `collectAsState()`, แสดง `name`, `studentCode`, `"Sync: ${syncStatus}"` และปุ่ม `Sync Now`
7. ทดสอบ: ปิดเน็ต → เพิ่ม → PENDING → เปิดเน็ต → Sync Now → ตรวจ PostgreSQL → SYNCED

**ต้องถามผู้ใช้ก่อนลงมือ:**
- (ก) `model/Student.kt` (Entity ปัจจุบัน) จะเปลี่ยนเป็น `data/local/StudentEntity.kt` ตามชีทไหม (ชีท Part 2 ข้อ H ก็แนะนำ `data/local/StudentEntity.kt` คู่กับ `model/Student.kt`) และ Lab 7 จะสร้าง `domain/model/Student.kt` ซึ่งชนชื่อกับ Entity ถ้ายังใช้ชื่อ `Student`
- (ข) **Room schema เปลี่ยน** (เพิ่มคอลัมน์ เปลี่ยน PK) ชีทไม่บอกวิธี ต้อง bump `version` และเลือก: migration, `fallbackToDestructiveMigration()` หรือให้ผู้ใช้ uninstall แอปก่อนทดสอบ
- (ค) ของเดิมจาก Part 2 ที่ชีท Lab 4 ไม่ได้กล่าวถึง: `getStudents()` ที่ `deleteAll()` ทุกครั้ง (**จะลบรายการ PENDING ทิ้ง** ถ้ายังเรียกอยู่), Refresh, Search, Edit/Delete ผ่าน API, หน้า Detail จะเก็บ ลบ หรือปรับอย่างไร (ชีทบอก "หน้าจออ่านจาก Room เป็นหลัก ไม่ควรผูกกับการเรียก Server ทุกครั้ง")
- (ง) ย้าย `ui/StudentScreen.kt` และ `viewmodel/StudentViewModel.kt` ไป `ui/student/` ตามชีท (ผู้ใช้เคยบอกให้ตามชีท จึงน่าจะใช่ แต่ยืนยันก่อน)

### Lab 5: WorkManager + Background Sync (`doc/LAB 5.pdf`, 11 ขั้น)

ตามชีท: dependency `androidx.work:work-runtime-ktx`, `AppContainer.kt` (root package: Room, TokenStorage, `RetrofitClient.create`, StudentApi, StudentRepository), `StudentApplication.kt` (`lateinit var appContainer`), Manifest `android:name=".StudentApplication"` ใน `<application>` เดิม, ตรวจ `syncPending()` ต้องปล่อย exception ให้ Worker retry, `worker/SyncWorker.kt` (CoroutineWorker: success / IOException→retry / Exception→failure), `worker/SyncScheduler.kt` (`syncNow` OneTime + NetworkType.CONNECTED; `schedulePeriodicSync` 15 นาที `enqueueUniquePeriodicWork("student-sync", UPDATE)`), ปุ่ม `Sync in Background` ใน StudentScreen (ใช้ `LocalContext`), เรียก `SyncScheduler.schedulePeriodicSync(this)` จาก `MainActivity`

ส่งงาน: ภาพปุ่ม, ภาพ Logcat ที่ Worker ทำงาน, ภาพ PostgreSQL หลัง sync, อธิบาย OneTimeWorkRequest vs PeriodicWorkRequest

**ต้องถามผู้ใช้ก่อนลงมือ:**
- (ก) ชีทใช้ชื่อ DB `"student.db"` แต่ของเราคือ `"student_database"` จะเปลี่ยนตามชีทไหม (ข้อมูล Room เดิมจะไม่ถูกใช้)
- (ข) **`MainActivity` ปัจจุบันสร้าง Room/Repository เอง** ถ้า Worker ใช้ของจาก `AppContainer` อีกชุด จะมี Room 2 instance ใน process เดียว และ Flow ในหน้าจอจะ **ไม่อัปเดตหลัง Worker sync** ชีทไม่ได้สั่งให้ MainActivity ใช้ `appContainer` ต้องถามว่าให้ MainActivity เปลี่ยนมาดึงจาก `(application as StudentApplication).appContainer` ไหม
- (ค) Logcat: ชีทให้ตรวจว่า Worker ถูกเรียก แต่โค้ดตัวอย่างไม่มี `Log` ถ้าต้องเพิ่ม `Log.d` ต้องถามก่อน (ดู Background Task Inspector ใน App Inspection แทนได้)

### Lab 6: Hilt (`doc/LAB 6.pdf`, 10 ขั้น)

ตามชีท: Hilt plugin + `hilt-android` + compiler + `hilt-navigation-compose` (ชีทเขียน kapt แต่บอก "ตามรูปแบบที่โปรเจกต์ใช้" โปรเจกต์ใช้ **KSP** ให้ใช้ `ksp("com.google.dagger:hilt-compiler")` และตรวจเวอร์ชันที่รองรับ Kotlin 2.2.10/KSP 2.x), `@HiltAndroidApp class StudentApplication : Application()` (เลิกใช้ AppContainer), `@AndroidEntryPoint` บน MainActivity, `di/DatabaseModule.kt` (AppDatabase, StudentDao), `di/NetworkModule.kt` (OkHttpClient + AuthInterceptor, Retrofit, StudentApi, AuthApi, TokenStorage provider) ใช้ `BASE_URL` 127.0.0.1, `StudentRepository @Inject constructor`, `@HiltViewModel StudentViewModel @Inject constructor`, `hiltViewModel()` ใน StudentScreen, ขั้น 9 `@HiltWorker` + `@AssistedInject` + `androidx.hilt:hilt-work` + `HiltWorkerFactory` (ต้องตั้ง `Configuration.Provider` ใน Application และปิด WorkManagerInitializer ใน Manifest ไม่งั้นไม่ทำงาน)

ส่งงาน: ภาพโครงสร้าง `di/`, ภาพ `@HiltAndroidApp`, ภาพ `@HiltViewModel`, อธิบายว่า Hilt ลดการสร้าง object แบบ manual อย่างไร

**ต้องถามผู้ใช้ก่อนลงมือ:**
- (ก) `LoginViewModel` จะแปลงเป็น Hilt ด้วยไหม (ชีท Lab 3 บอก "จะทำใน Lab 6 ด้วย Hilt" และ NetworkModule มี `provideAuthApi` แต่ชีท Lab 6 แสดงแค่ StudentViewModel)
- (ข) `RetrofitClient.kt` และ `AppContainer.kt` ลบหลังย้ายไป Hilt หรือไม่ (ชีทบอกว่า AppContainer "อาจลบได้" หลังตรวจว่าไม่มีใครใช้)

### Lab 7: Testing + Clean Architecture (`doc/LAB 7.pdf`, 10 ขั้น)

ตามชีท: `domain/model/Student.kt` (id, studentCode, name, major), `domain/repository/StudentRepository.kt` (interface: `observeStudents(): Flow<List<Student>>`, `addStudent`, `syncPending`), `domain/usecase/GetStudentsUseCase.kt` และ `SyncStudentsUseCase.kt` (`@Inject constructor`, `operator fun invoke`), `data/repository/StudentRepositoryImpl.kt` (map Entity→Domain `id = serverId ?: localId`, ย้าย sync logic จริงเข้ามา ห้ามปล่อยเป็น comment), `di/RepositoryModule.kt` (`@Binds @Singleton`), ViewModel รับ Use Case แทน Repository, Unit test `app/src/test/java/com/example/studentmanagement/domain/usecase/GetStudentsUseCaseTest.kt` + `FakeStudentRepository` (ต้องเพิ่ม `kotlinx-coroutines-test`; junit มีแล้ว)

ส่งงาน: ภาพ package 3 ชั้น, Use Case อย่างน้อย 2 ไฟล์, Repository Interface + Impl, ผล Unit Test, อธิบายว่าทำไม Domain ไม่ควรขึ้นกับ Room/Retrofit

**ต้องถามผู้ใช้ก่อนลงมือ:**
- (ก) ViewModel ในชีทมีแค่ `students` + `syncStudents()` แต่หน้าจอจาก Lab 4 ยังมีปุ่ม Add (และ Sync in Background จาก Lab 5) จะให้ ViewModel เรียก `addStudent` ผ่านอะไร (ชีทไม่มี AddStudentUseCase; การเพิ่มต้องถาม)
- (ข) `SyncWorker` จะให้เรียก `SyncStudentsUseCase` ตาม `การรวม Lab 3_7.pdf` หรือคงเรียก Repository ตามชีท Lab 6
- (ค) Unit test ของ ViewModel: ชีทระบุในจุดประสงค์และตารางทดสอบ แต่ไม่ให้โค้ดตัวอย่าง ถามก่อนว่าจะเขียนหรือไม่

### Lab 10 (เดิม Lab 8): Multimedia and Sensors (`doc/Lab 10.pdf`, 21 STEP)

ตามชีท: package `ui.camera`, `ui.music`, `ui.video`, `ui.sensor`, `ui.home`, `sensor/StepCounterManager.kt`; CameraX (`camera-camera2`, `camera-lifecycle`, `camera-view` เวอร์ชันเดียวกัน); permission `CAMERA` และ `ACTIVITY_RECOGNITION` ใน Manifest; `CameraScreen` (PreviewView ผ่าน AndroidView, runtime permission, overlay filter) และ **งานบังคับ `***` เพิ่ม Filter Warm, Cool, Vintage, Dark**; `res/raw/music.mp3` + `MusicScreen` (MediaPlayer Play/Pause/Stop, release ใน DisposableEffect); `res/raw/campus.mp4` + `VideoScreen` (VideoView ใน AndroidView); `StepCounterManager` + `StepCounterScreen` และ **งานบังคับ `***` เป้าหมาย 10,000 ก้าว, `progress = steps / 10000f` กับ `LinearProgressIndicator()`**; `HomeScreen` 4 ปุ่ม; STEP 21 Navigation route `home/camera/music/video/sensor` (ต้องเพิ่ม `navigation-compose`)

- ใช้ `com.example.studentmanagement.R` สำหรับ `R.raw.*`
- ไฟล์ `music.mp3` และ `campus.mp4` **ผู้ใช้ต้องจัดหาเอง** ห้ามดาวน์โหลดเองโดยไม่ได้รับอนุญาต

**ต้องถามผู้ใช้ก่อนลงมือ:**
- (ก) ใช้ชีทนี้แทน Lab 8 และชื่อ branch (ดูหัวข้อ 3)
- (ข) **HomeScreen อยู่ตรงไหนของ flow เดิม** (Login → StudentScreen) ชีทไม่บอก เช่น Home เป็นหน้าแรกหลัง Login แล้วมีปุ่มไป Student หรือไม่ (การเพิ่มปุ่มไป Student เกินชีท)
- (ค) **Android 10+ ต้องขอ `ACTIVITY_RECOGNITION` ตอน runtime** ไม่งั้น step counter เป็น 0 บนมือถือผู้ใช้ (API 33) ชีทให้แค่ประกาศใน Manifest ต้องถามก่อนเพิ่ม runtime request
- (ง) Mockup มี bottom bar "Home Camera Music Sensor" แต่โค้ดตัวอย่างใช้ปุ่มใน Column ให้ทำตามโค้ด เว้นแต่ผู้ใช้สั่ง

### Lab 11 (เดิม Lab 9): Animation and Motion (`doc/Lab 11.pdf`, 13 STEP)

ตามชีท (แก้ `ui/home/HomeScreen.kt`): PART 1 `animateDpAsState` Card 120↔220dp + ปุ่ม Expand/Collapse; PART 2 `AnimatedVisibility` "🎉 Keep Going!" + ปุ่ม Show Message + `fadeIn()+slideInVertically()` / `fadeOut()+slideOutVertically()`; PART 3 `updateTransition` + `animateFloat` scale (1.05/1) และ alpha (1/0.7) + `graphicsLayer` + `clickable`; PART 4 `lottie-compose`, `res/raw/loading.json`, `success.json`, `walking.json`, `ui/animation/LoadingAnimation.kt` (`IterateForever`, size 180dp) และ **งานบังคับ `***` เปลี่ยน Loading → Walking เมื่อ `steps >= 1000`**; PART 5 Screen Transition: Camera → Slide, Music → Fade, Video → Scale, Sensor → Slide (ใช้ API ของ Navigation Compose เวอร์ชันโปรเจกต์); Final: Header (Fade + Slide), Step Card (Scale + Progress Animation), Feature Cards (AnimatedVisibility), Loading (Lottie), Navigation (Screen Transition); Project Challenge: ต้องมี animation ครบ 5 ประเภท

- ไฟล์ Lottie JSON ผู้ใช้ต้องจัดหาเอง

**ต้องถามผู้ใช้ก่อนลงมือ:**
- (ก) HomeScreen ต้องรู้ค่า steps (Step Card และ Loading/Walking) แต่ Lab 10 ให้ `StepCounterManager` อยู่ใน `StepCounterScreen` ชีทไม่บอกวิธีส่งค่ามาที่ Home ต้องถามวิธีที่ผู้ใช้ต้องการ
- (ข) "Hello, Student" ใช้ชื่อคงที่ตาม mockup หรือชื่อผู้ใช้ที่ login (อย่างหลังเกินชีท)

## 5. บทเรียนจากแล็บก่อน (ห้ามพลาดซ้ำ)

- Lab 3 เคยเพิ่ม Logout, จัดการ 401, ข้อความ error แยกหลายแบบ, ล้าง cache ตอน logout และเขียน comment เอง แล้ว **ผู้ใช้สั่งให้ถอดออกทั้งหมด** ต่อไปถามก่อนเสมอ
- Lab 3 เคยวางไฟล์ไม่ตรงชีท (`viewmodel/LoginViewModel.kt` แทน `ui/login/`) ต้องรีแฟคเตอร์ ต้องเช็กตำแหน่งไฟล์กับชีททุกครั้ง
- `TokenResponse` ใช้ชื่อ field ตามชีท (`access_token`, `token_type`) ไม่ใช่ `@SerializedName`
- ชีทไม่ได้ให้โค้ด backend สำหรับ Lab 3 ผู้ใช้ยอมรับว่า backend ที่จำเป็น (`/token`, ตรวจ JWT, `seed_user.py`) ต้องมี แล็บหลังถ้าต้องแก้ backend เกินที่ชีทให้ ให้บอกและถามก่อน
- ผู้ใช้เคยลบ `Co-Authored-By` ออกจากประวัติ git ทั้งหมด เพราะไม่ต้องการให้ Claude เป็น contributor (อีกเหตุผลที่ห้าม commit เอง)
- ภาพส่งงานใส่ในโฟลเดอร์ `submission/` (ผู้ใช้จัดการเอง)
