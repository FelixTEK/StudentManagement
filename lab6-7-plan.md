# แผน Lab 6 (Hilt) และ Lab 7 (Clean Architecture + Unit Test)

อ่านจาก `doc/LAB 6.pdf` และ `doc/LAB 7.pdf` ครบทุกขั้นแล้ว ยังไม่ได้แก้โค้ดใด ๆ

## สถานะ branch

| Branch | ชี้ไปที่ | หมายเหตุ |
|---|---|---|
| `lab5` | `2aba10b` | commit และ push แล้ว |
| `lab6` | `2aba10b` | สร้างแล้ว ยังไม่ push |
| `lab7` | - | ยังไม่สร้าง (ลบทิ้งตามคำสั่งผู้ใช้) |

ตอนนี้ `lab6` ชี้ commit เดียวกับ `lab5` ซึ่งยังไม่มีงานของ Lab 6 ผู้ใช้ยังอยู่บน `lab5`

**ลำดับที่ต้องทำ:** สลับไป `lab6` ทำ Lab 6 และ commit ให้เสร็จ จากนั้นค่อยสร้าง `lab7` จาก `lab6` (Lab 7 ต่อยอดโค้ดของ Lab 6):

```powershell
git switch lab6
git switch -c lab7 lab6
```

(บรรทัดแรกใช้ตอนเริ่ม Lab 6 บรรทัดที่สองใช้ตอนเริ่ม Lab 7 หลัง commit Lab 6 แล้ว ยังไม่ได้สลับ branch ให้ เพราะผู้ใช้สั่งให้รอ)

---

## สิ่งที่ต้องส่งนอกเหนือจากตัวโปรแกรม

### Lab 6
1. ภาพโครงสร้างโฟลเดอร์ `di/`
2. ภาพโค้ด `@HiltAndroidApp` (`StudentApplication.kt`)
3. ภาพโค้ด `@HiltViewModel` (`StudentViewModel.kt`)
4. คำอธิบายว่า Hilt ช่วยลดการสร้าง Object แบบ Manual อย่างไร

### Lab 7
1. ภาพโครงสร้าง Package ทั้ง 3 ชั้น (presentation / domain / data)
2. ไฟล์ Use Case อย่างน้อย 2 ไฟล์ (`GetStudentsUseCase.kt`, `SyncStudentsUseCase.kt`)
3. ไฟล์ Repository Interface และ Implementation (`domain/repository/StudentRepository.kt`, `data/repository/StudentRepositoryImpl.kt`)
4. ผล Unit Test (ภาพหน้าจอผลรันผ่าน)
5. คำอธิบายว่าเหตุใด Domain จึงไม่ควรขึ้นกับ Room หรือ Retrofit โดยตรง

ภาพส่งงานใส่ใน `submission/` (ผู้ใช้จัดการเอง)

---

## Lab 6: Hilt (10 ขั้น)

| ขั้น | ตามชีท | ไฟล์ |
|---|---|---|
| 1 | เพิ่ม Hilt plugin, `hilt-android`, compiler, `hilt-navigation-compose` | `build.gradle.kts` (root และ app), `libs.versions.toml` |
| 2 | `@HiltAndroidApp class StudentApplication : Application()` เลิกใช้ AppContainer | `StudentApplication.kt` |
| 3 | `@AndroidEntryPoint` บน `MainActivity` | `MainActivity.kt` |
| 4 | `DatabaseModule` (AppDatabase `"student.db"`, StudentDao) | `di/DatabaseModule.kt` |
| 5 | `NetworkModule` (OkHttpClient + AuthInterceptor, Retrofit, StudentApi, AuthApi, TokenStorage) | `di/NetworkModule.kt` |
| 6 | `StudentRepository @Inject constructor(dao, api)` | `data/repository/StudentRepository.kt` |
| 7 | `@HiltViewModel StudentViewModel @Inject constructor(repository)` | `ui/student/StudentViewModel.kt` |
| 8 | `val viewModel: StudentViewModel = hiltViewModel()` | `ui/student/StudentScreen.kt` |
| 9 | `@HiltWorker` + `@AssistedInject` + `HiltWorkerFactory` + `Configuration.Provider` | `worker/SyncWorker.kt`, `StudentApplication.kt` |
| 10 | ทดสอบ build, DB, ViewModel, API, Worker | |

### จุดที่ต้องปรับให้เข้ากับโปรเจกต์ (ชีทบอกให้ปรับเองหรือไม่ตรงกับของเรา)
- ชีทเขียน `kapt` แต่โปรเจกต์ใช้ **KSP** ต้องใช้ `ksp("com.google.dagger:hilt-compiler:...")` และไม่เพิ่ม `kotlin-kapt`
- เวอร์ชัน Hilt ต้องเลือกให้เข้ากับ Kotlin 2.2.10, KSP 2.2.10-2.0.2, AGP 8.8.2 ชีทห้ามคัดลอกเลขจากที่อื่น ต้องตรวจตอน build
- ชีทใช้ `http://10.0.2.2:8000/` แต่เราใช้ **`http://127.0.0.1:8000/`** (มือถือจริง + `adb reverse`)
- ชีทข้อ 6 ในไฟล์ PDF โค้ด `@Inject constructor` พิมพ์เพี้ยน ใช้รูปแบบ `class StudentRepository @Inject constructor(private val dao: StudentDao, private val api: StudentApi)`
- ชื่อฐานข้อมูลคือ `"student.db"` เหมือน Lab 5 อยู่แล้ว จึงไม่ต้อง uninstall/pm clear
- ขั้น 9 (Hilt กับ WorkManager) ชีทระบุว่า "ต้องตั้งค่า WorkerFactory และ Configuration" ต้องใช้ `androidx.hilt:hilt-work` (+ compiler ผ่าน KSP) ตั้ง `Configuration.Provider` ใน `StudentApplication` ด้วย `HiltWorkerFactory` และ **ปิด initializer เดิมของ WorkManager ใน Manifest** ไม่งั้น Worker ไม่ทำงาน (ชีทไม่ได้ให้โค้ดส่วนนี้ ต้องถามก่อนเขียนเกินตัวอย่าง)
- `AppContainer.kt` และ `RetrofitClient.kt` ลบได้เมื่อไม่มีใครเรียกใช้แล้ว (ชีทเขียนว่า "อาจลบได้")

### คำถามที่ต้องถามผู้ใช้ก่อนลงมือ Lab 6
1. **LoginViewModel** จะแปลงเป็น Hilt ด้วยไหม ตอนนี้ `MainActivity` สร้าง `LoginViewModel`, `TokenStorage`, `RetrofitClient.create(...)` เอง และใช้ `tokenStorage.getToken()` ตัดสินว่าจะแสดงหน้า Login หรือ Student ชีท Lab 3 บอกว่าจะทำใน Lab 6 และ `NetworkModule` มี `provideAuthApi` แต่ชีท Lab 6 ไม่ได้แสดงวิธีแก้ `MainActivity` ถ้าตัด `RetrofitClient` ออก ส่วน Login ต้องได้ `AuthApi`/`TokenStorage` จาก Hilt ด้วย
2. **StudentScreen** ตอนนี้รับ `viewModel` เป็นพารามิเตอร์จาก `MainActivity` ชีทข้อ 8 ให้สร้างด้วย `hiltViewModel()` ภายใน จะเปลี่ยน signature เป็น `viewModel: StudentViewModel = hiltViewModel()` ใช่ไหม
3. **ขั้น 9 (Hilt Worker)** ทำหรือไม่ ชีทเรียกว่า "ขั้นต่อยอด" แต่ `การรวม Lab 3_7.pdf` และการที่ Worker ต้องใช้ Repository หลังลบ `AppContainer` ทำให้ต้องทำ ถ้าทำ ต้องยืนยันว่าให้เพิ่มการตั้งค่า Manifest/Application ที่ชีทไม่ได้ให้โค้ดตรง ๆ
4. ลบ `AppContainer.kt` และ `RetrofitClient.kt` หรือไม่

---

## Lab 7: Clean Architecture + Unit Test (10 ขั้น)

| ขั้น | ตามชีท | ไฟล์ |
|---|---|---|
| 1 | Domain model `Student(id, studentCode, name, major)` | `domain/model/Student.kt` |
| 2 | Repository interface: `observeStudents(): Flow<List<Student>>`, `addStudent(Student)`, `syncPending()` | `domain/repository/StudentRepository.kt` |
| 3 | Use Case 2 ตัว (`operator fun invoke`) | `domain/usecase/GetStudentsUseCase.kt`, `SyncStudentsUseCase.kt` |
| 4 | `StudentRepositoryImpl` map Entity → Domain (`id = serverId ?: localId`) และ **ย้าย Logic Sync จริงจาก Lab 4–5 มาไว้** (ห้ามปล่อยเป็น comment) | `data/repository/StudentRepositoryImpl.kt` |
| 5 | `@Binds @Singleton` ผูก interface กับ Impl | `di/RepositoryModule.kt` |
| 6 | ViewModel รับ Use Case 2 ตัวแทน Repository | `ui/student/StudentViewModel.kt` |
| 7 | เพิ่ม `@Inject constructor` ให้ Use Case ทั้งสอง | `GetStudentsUseCase.kt`, `SyncStudentsUseCase.kt` |
| 8 | `FakeStudentRepository` + `GetStudentsUseCaseTest` (`runTest`, `first()`, `assertEquals`) | `app/src/test/java/com/example/studentmanagement/domain/usecase/GetStudentsUseCaseTest.kt` |
| 9 | ตารางทดสอบ (ดูด้านล่าง) | |
| 10 | ตารางสรุป 3 ชั้น | (เนื้อหาอ่านอย่างเดียว) |

### จุดที่ต้องปรับให้เข้ากับโปรเจกต์
- ทุก path ในชีทเขียน `com.example.studentapp` ให้ใช้ `com.example.studentmanagement`
- ต้องเพิ่ม dependency `kotlinx-coroutines-test` (ชีทบอกเองว่าให้เพิ่ม) JUnit มีอยู่แล้ว
- `data/repository/StudentRepository.kt` (คลาสเดิม) ชื่อชนกับ interface ใหม่ใน domain ต้องแทนที่ด้วย `StudentRepositoryImpl` (ย้าย logic sync ที่ใช้งานได้มาไว้ครบ)
- โค้ด Use Case ในขั้น 3 ไม่มี `@Inject` ขั้น 7 ถึงเพิ่ม จึงเขียนให้มีตั้งแต่ต้นได้ (ผลเหมือนกัน)
- `SyncWorker` ต้องปรับ import จาก Repository เดิมเป็น interface ของ domain
- หมายเหตุ: ชีทให้ `id = entity.serverId ?: entity.localId` ซึ่งอาจซ้ำกันระหว่างรายการที่ sync แล้วและยังไม่ได้ sync ชีทไม่ได้พูดถึง ไม่แก้เว้นแต่ผู้ใช้สั่ง

### ตารางทดสอบขั้น 9 (ชีทระบุ)

| ทดสอบ | ผลที่คาดหวัง |
|---|---|
| `GetStudentsUseCase` | คืนรายการนักศึกษาได้ |
| `SyncStudentsUseCase` | เรียก Repository Sync |
| `StudentViewModel` | แสดง State จาก Use Case |
| Repository Implementation | แปลง Entity เป็น Domain Model ได้ |
| Offline | อ่านข้อมูลจาก Room ได้ |
| Online | Sync ข้อมูลไป Server ได้ |

### คำถามที่ต้องถามผู้ใช้ก่อนลงมือ Lab 7
1. **ปุ่ม Add นักศึกษา** ViewModel ในชีทมีแค่ `students` กับ `syncStudents()` แต่หน้าจอยังมีปุ่ม `+ Add Student` (เรียก `viewModel.addStudent(StudentEntity)`) และ interface ใน domain มี `addStudent(Student)` อยู่ ชีทไม่มี `AddStudentUseCase` จะให้ทำอย่างไร: เพิ่ม `AddStudentUseCase` (เกินชีท 1 ไฟล์) / ให้ ViewModel เรียก repository ตรง (ผิดหลัก Clean) / ตัดปุ่มออก (ใช้ทดสอบไม่ได้)
2. **SyncWorker** จะเรียก `SyncStudentsUseCase` (ตามภาพรวม `การรวม Lab 3_7.pdf`) หรือคงเรียก Repository (ตามชีท Lab 6)
3. **Unit test ของ ViewModel** ชีทระบุในจุดประสงค์และตารางทดสอบ แต่ไม่ให้โค้ดตัวอย่าง จะเขียนหรือไม่ (รวมถึงทดสอบ `SyncStudentsUseCase` และ `StudentRepositoryImpl`) ชีทให้โค้ดทดสอบเพียง `GetStudentsUseCase` ตัวเดียว
4. ปุ่ม `Sync in Background` และ `Sync Now` ใน `StudentScreen` ยังคงอยู่ใช่ไหม (`Sync Now` เรียก `viewModel.syncStudents()` ตรงกับชีท)

---

## ข้อควรระวังที่ใช้ตลอด Lab 6–7
- ห้ามเขียน comment ในโค้ด (ยกเว้นที่มีในโค้ดตัวอย่างของชีท)
- ห้ามทำเกินชีท ถ้าจำเป็นต้องเพิ่มให้ถามก่อน
- ผู้ใช้ commit เอง ผมไม่รัน `git add`/`commit`/`push`/`tag`
- ห้ามอ่านค่าใน `student-api/.env`
- คำสั่งที่ให้ผู้ใช้รันเป็น PowerShell ทั้งหมด
- การทดสอบบนมือถือ: เปิด uvicorn (`.\venv\Scripts\python.exe -m uvicorn main:app --reload` ใน `student-api`) และ `adb reverse tcp:8000 tcp:8000` ก่อน
