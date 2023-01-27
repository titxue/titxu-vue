export interface Window {
  existLoading?: boolean;
  lazy?: NodeJS.Timer;
  unique?: number;
  tokenRefreshing?: boolean;
  requests?: Function[];
  eventSource?: EventSource;
  loadLangHandle?: Record<string, any>;
}
