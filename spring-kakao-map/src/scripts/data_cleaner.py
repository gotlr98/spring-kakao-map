import pandas as pd
import json
import random

# CSV 파일 읽기 (한글 인코딩 처리)
try:
    df = pd.read_csv('식품_일반음식점_인천중구.csv', encoding='cp949')
except:
    df = pd.read_csv('식품_일반음식점_인천중구.csv', encoding='utf-8')

# 1. 영업 중인 가게만 필터링 ('영업/정상')
active_df = df[df['영업상태명'] == '영업/정상'].copy()

# 2. 필요한 데이터 추출 및 랜덤 샘플링 (30개)
# 데이터가 30개보다 적으면 전체 사용
sample_size = min(30, len(active_df))
sampled_df = active_df.sample(n=sample_size, random_state=42)

# 3. 인천 중구 대략적인 범위 (위도/경도)
# 인천 중구 (대략 37.4 ~ 37.5, 126.4 ~ 126.6)
incheon_lat_min, incheon_lat_max = 37.4500, 37.5000
incheon_lon_min, incheon_lon_max = 126.4000, 126.6000

result_json = []

for index, row in sampled_df.iterrows():
    # 카테고리가 비어있으면 '기타'로 채움
    category = row['업태구분명'] if pd.notna(row['업태구분명']) else row['위생업태명']
    if pd.isna(category):
        category = "일반음식점"

    # 좌표는 랜덤 생성 (실제 좌표계 변환이 복잡하므로 시뮬레이션용)
    simulated_lat = random.uniform(incheon_lat_min, incheon_lat_max)
    simulated_lon = random.uniform(incheon_lon_min, incheon_lon_max)

    place = {
        "name": str(row['사업장명']),
        "category": str(category),
        "lat": round(simulated_lat, 6), # 소수점 6자리까지
        "lon": round(simulated_lon, 6)
    }
    result_json.append(place)

# JSON 결과 출력
print(json.dumps(result_json, ensure_ascii=False, indent=2))