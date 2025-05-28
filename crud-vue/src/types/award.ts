export interface Award {
  id: number;
  name: string;
  quantity: number;
  prize: string;
}

export interface AwardRecord {
  id: number;
  userId: number;
  awardId: number;
  awardName: string;
  prize: string;
  createTime: string;
}